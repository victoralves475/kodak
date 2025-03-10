package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.CommentService;
import br.edu.ifpb.kodak.service.PhotoService;
import br.edu.ifpb.kodak.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotographerService photographerService;

    @PostMapping("/new")
    public String newComment(@RequestParam("photoId") Integer photoId,
                             @RequestParam("commentText") String commentText,
                             Model model, RedirectAttributes redirectAttributes) {
        // Recupera a autenticação e o email do usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        // Recupera a foto
        Photo photo = photoService.getPhotoById(photoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        // Se o fotógrafo logado for o dono da foto, não permite comentar
        if (loggedPhotographer.getId() == photo.getPhotographer().getId()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Você não pode comentar em suas próprias fotos.");
            return "redirect:/photo/post?photoId=" + photoId;
        }

        // Adiciona o comentário e redireciona para a página da foto
        commentService.addCommentToPhoto(commentText, photo, loggedPhotographer);
        return "redirect:/photo/post?photoId=" + photoId;
    }
}
