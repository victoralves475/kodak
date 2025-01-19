package br.edu.ifpb.kodak.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.kodak.model.Hashtag;
import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.FileStorageService;
import br.edu.ifpb.kodak.service.HashtagService;
import br.edu.ifpb.kodak.service.PhotoService;
import br.edu.ifpb.kodak.service.PhotographerService;
import jakarta.validation.Valid;

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

//    @GetMapping("/form")
//    public String getForm(Photographer photographer, Model model) {
//        model.addAttribute("photographer", photographer);
//        return "/photographer/form";
//    }
//
//    @PostMapping
//    public ModelAndView savePhotographer(@Valid Photographer photographer, BindingResult result, ModelAndView model, RedirectAttributes attr) {
//        if (result.hasErrors()) {
//            model.setViewName("/photographer/form");
//            return model;
//        }
//        photographerService.savePhotographer(photographer);
//        attr.addFlashAttribute("mensagem", "Fotógrafo " + photographer.getName() + " salvo com sucesso!");
//        model.setViewName("redirect:/");
//        return model;
//    }

    @GetMapping("/home")
    public String home(@RequestParam("photographerId") Integer photographerId, Model model) {
        Photographer photographer = photographerService.getPhotographerById(photographerId)
                .orElseThrow(() -> new RuntimeException("Fotógrafo não encontrado"));

        model.addAttribute("photographer", photographer);

        return "photographer/home";
    }
    
    @GetMapping("/new-photo")
    public String newPhotoPage(@RequestParam("photographerId") Integer photographerId, Model model) {
        model.addAttribute("photographerId", photographerId);
        return "photographer/new-photo"; // Nome do arquivo HTML na pasta templates
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

            // Cria e persiste a entidade Photo
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

}
