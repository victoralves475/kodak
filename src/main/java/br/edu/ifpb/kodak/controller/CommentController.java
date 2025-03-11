package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.model.Comment;
import br.edu.ifpb.kodak.service.CommentService;
import br.edu.ifpb.kodak.service.PhotoService;
import br.edu.ifpb.kodak.service.PhotographerService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

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

        // Se o comentário estiver vazio, não permite comentar
        if (commentText.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Comentário não pode estar vazio.");
            return "redirect:/photo/post?photoId=" + photoId;
        }

        // Adiciona o comentário e redireciona para a página da foto
        commentService.addCommentToPhoto(commentText, photo, loggedPhotographer);

        return "redirect:/photo/post?photoId=" + photoId;
    }

    @PostMapping("/update/{id}")
    public String updateComment(@PathVariable Integer id,
                                @RequestParam("commentText") String newText,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        // Recupera a autenticação e o email do usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        Optional<Comment> optionalComment = commentService.getCommentById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            int photoId = comment.getPhoto().getId();
            boolean updated = commentService.updateComment(id, loggedPhotographer, newText);

            // Se o comentário estiver vazio, exclui o comentário diretamente
            if (newText.isBlank()) {
                boolean deleted = commentService.deleteComment(id, loggedPhotographer);
                if (deleted) {
                    redirectAttributes.addFlashAttribute("successMessage", "Comentário removido por estar vazio.");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover comentário.");
                }
                return "redirect:/photo/post?photoId=" + photoId;
            }

            if (updated) {
                redirectAttributes.addFlashAttribute("successMessage", "Comentário atualizado com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar comentário.");
            }
            return "redirect:/photo/post?photoId=" + photoId; // Redireciona para a página da foto
        }
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Integer commentId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Photographer loggedPhotographer = (Photographer) session.getAttribute("loggedPhotographer");

        Optional<Comment> optionalComment = commentService.getCommentById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            int photoId = comment.getPhoto().getId();
            boolean deleted = commentService.deleteComment(commentId, loggedPhotographer);
            System.out.println("deletou: " + deleted);

            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "Comentário excluído com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir comentário.");
            }
            return "redirect:/photo/post?photoId=" + photoId; // Redireciona para a página da foto
        }


        return "redirect:/home"; // Redireciona para a página da foto
    }

}
