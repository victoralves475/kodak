package br.edu.ifpb.kodak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotoService;
import br.edu.ifpb.kodak.service.PhotographerService;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotographerService photographerService;

    @GetMapping("/post")
    public String postPage(@RequestParam("photoId") Integer photoId, Model model) {
        Photo photo = photoService.getPhotoById(photoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));
        model.addAttribute("photo", photo);

        // Recupera o usuário autenticado via SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        boolean owner = loggedPhotographer.getId() == photo.getPhotographer().getId();
        model.addAttribute("owner", owner);

        // Verifica se o usuário autenticado possui a role "ADMIN"
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        return "photo/post";
    }

    @PostMapping("/like")
    public String likePhoto(@RequestParam("photoId") Integer photoId, Model model, RedirectAttributes redirectAttributes) {
        // Recupera a foto pelo ID
        Photo photo = photoService.getPhotoById(photoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        // Recupera o fotógrafo logado via SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        // Permite curtir a foto apenas se o fotógrafo não for o dono
        if (loggedPhotographer.getId() != photo.getPhotographer().getId()) {
            photographerService.likePhoto(photoId, loggedPhotographer);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Você não pode curtir a própria foto");
        }

        // Redireciona para a página da foto
        return "redirect:/photo/post?photoId=" + photoId;
    }
}
