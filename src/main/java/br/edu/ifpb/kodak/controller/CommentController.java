package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Photo;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.kodak.model.Comment;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.repository.PhotoRepository;
import br.edu.ifpb.kodak.service.CommentService;
import br.edu.ifpb.kodak.service.PhotoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PhotoService photoService;

    @PostMapping("/new")
    public String newComment(@RequestParam("photoId") Integer photoId,
                             @RequestParam("commentText") String commentText, Model model, HttpSession session) {
        Photographer loggedPhotographer = (Photographer) session.getAttribute("loggedPhotographer");

        Photo photo = photoService.getPhotoById(photoId).orElse(null);

        if (loggedPhotographer.getId() == photo.getPhotographer().getId()) {
            model.addAttribute("errorMessage", "Você não pode comentar em suas próprias fotos.");
            return "redirect:/photo/post?photoId=" + photoId;
        }

        commentService.addCommentToPhoto(commentText, photo, loggedPhotographer);
        
        return "redirect:/photo/post?photoId=" + photoId;
    }

    @PostMapping("/update/{id}")
    public String updateComment(@PathVariable Integer id,
                                @RequestParam("commentText") String newText,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Photographer loggedPhotographer = (Photographer) session.getAttribute("loggedPhotographer");

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
            System.out.println("deletou: " +deleted);

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