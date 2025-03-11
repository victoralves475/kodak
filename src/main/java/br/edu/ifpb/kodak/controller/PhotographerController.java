package br.edu.ifpb.kodak.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.kodak.model.Hashtag;
import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.FileStorageService;
import br.edu.ifpb.kodak.service.HashtagService;
import br.edu.ifpb.kodak.service.PhotoService;
import br.edu.ifpb.kodak.service.PhotographerService;

@Controller
@RequestMapping("/photographer")
public class PhotographerController {

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private HashtagService hashtagService;

    /**
     * Página inicial do fotógrafo.
     * Recupera o fotógrafo logado através do SecurityContext e o fotógrafo
     * da página que está sendo visitada pelo parâmetro.
     */
    @GetMapping("/home")
    public String home(@RequestParam("photographerId") Integer photographerId, Model model) {
        // Obtém o fotógrafo da página (perfil a ser visualizado)
        Photographer photographerHome = photographerService.getPhotographerById(photographerId)
                .orElseThrow(() -> new RuntimeException("Fotógrafo não encontrado"));

        // Recupera o usuário autenticado via SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        // Define flags para o template
        boolean follower = photographerHome.getFollowers().contains(loggedPhotographer);
        boolean owner = loggedPhotographer.getId() == photographerHome.getId();
        // Se o perfil estiver "bloqueado" para seguimento, desabilita a opção de seguir
        boolean follow = !photographerHome.isLockedFollow();

        model.addAttribute("owner", owner);
        model.addAttribute("photographer", photographerHome);
        model.addAttribute("follow", follow);
        model.addAttribute("follower", follower);

        return "photographer/home";
    }

    @GetMapping("/new-photo")
    public String newPhotoPage(@RequestParam("photographerId") Integer photographerId, Model model) {
        model.addAttribute("photographerId", photographerId);
        return "photographer/new-photo";
    }

    @PostMapping("/upload-photo")
    public String uploadPhoto(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("photographerId") Integer photographerId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "hashtags", required = false) String hashtags,
            RedirectAttributes redirectAttributes) {

        try {
            // Salva a foto no sistema de arquivos
            String photoPath = fileStorageService.savePhoto(photo, photographerId);

            // Cria a entidade Photo
            Photo newPhoto = new Photo();
            newPhoto.setPath(photoPath);
            newPhoto.setDescription(description);

            Photographer photographer = photographerService.getPhotographerById(photographerId)
                    .orElseThrow(() -> new RuntimeException("Fotógrafo não encontrado"));
            newPhoto.setPhotographer(photographer);

            if (hashtags != null && !hashtags.isEmpty()) {
                Set<Hashtag> hashtagSet = Arrays.stream(hashtags.split(","))
                        .map(String::trim)
                        .map(tag -> hashtagService.findOrCreateByName(tag))
                        .collect(Collectors.toSet());
                newPhoto.setTags(hashtagSet);
            }

            photoService.savePhoto(newPhoto);
            redirectAttributes.addFlashAttribute("successMessage", "Foto publicada com sucesso!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao publicar a foto: " + e.getMessage());
        }

        return "redirect:/photographer/home?photographerId=" + photographerId;
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Photographer> searchPhotographers(@RequestParam String name) {
        return photographerService.getPhotographerByName(name);
    }

    @PostMapping("/followUser")
    public String followPhotographer(@RequestParam("photographerId") Integer photographerId,
                                     RedirectAttributes redirectAttributes) {
        // Recupera o fotógrafo da página a ser seguido/desseguido
        Photographer photographerHome = photographerService.getPhotographerById(photographerId)
                .orElseThrow(() -> new RuntimeException("Fotógrafo não encontrado"));

        // Recupera o fotógrafo logado via SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        // Alterna a condição de seguir/desseguir
        if (photographerHome.getFollowers().contains(loggedPhotographer)) {
            photographerHome.getFollowers().remove(loggedPhotographer);
            loggedPhotographer.getFollowing().remove(photographerHome);
        } else {
            photographerHome.getFollowers().add(loggedPhotographer);
            loggedPhotographer.getFollowing().add(photographerHome);
        }

        photographerService.savePhotographer(photographerHome);
        photographerService.savePhotographer(loggedPhotographer);

        return "redirect:/photographer/home?photographerId=" + photographerId;
    }

    @PostMapping("/lockStatus")
    public String updateLockStatus(RedirectAttributes redirectAttributes) {
        // Recupera o fotógrafo logado via SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        photographerService.changeLockStatus(loggedPhotographer.getId());
        return "redirect:/photographer/home?photographerId=" + loggedPhotographer.getId();
    }
}
