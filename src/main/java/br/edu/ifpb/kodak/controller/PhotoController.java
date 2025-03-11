package br.edu.ifpb.kodak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import br.edu.ifpb.kodak.model.Comment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotoService;
import br.edu.ifpb.kodak.service.PhotographerService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/photo")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotographerService photographerService;

    @GetMapping("/post")
    public String postPage(@RequestParam("photoId") Integer photoId, Model model, HttpSession session) {
        Photo photo = photoService.getPhotoById(photoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));
        model.addAttribute("photo", photo);

        Photographer loggedPhotographer = (Photographer) session.getAttribute("loggedPhotographer");
        boolean owner = true;

        if (loggedPhotographer.getId() != photo.getPhotographer().getId()) {
            owner = false;
        }
        model.addAttribute("owner", owner);
        model.addAttribute("isAdmin", loggedPhotographer.isAdmin());
        model.addAttribute("loggedPhotographer", loggedPhotographer);

        return "photo/post";
    }

    @PostMapping("/like")
    public String likePhoto(@RequestParam("photoId") Integer photoId, Model model, HttpSession session) {
        // Recupera a foto pelo ID
        Photo photo = photoService.getPhotoById(photoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        // Recupera o fotógrafo logado da sessão
        Photographer loggedPhotographer = (Photographer) session.getAttribute("loggedPhotographer");
        Photographer fotografo = photographerService.getPhotographerById(loggedPhotographer.getId()).get();

        // Chama o método para curtir/descurtir a foto
        if (loggedPhotographer.getId() != photo.getPhotographer().getId()) {
            photographerService.likePhoto(photoId, fotografo);
        } else {
            model.addAttribute("errorMessage", "Você não pode curtir a própria foto");
        }
        
        // Redireciona para a página da foto
        return "redirect:/photo/post?photoId=" + photoId;
    }


}