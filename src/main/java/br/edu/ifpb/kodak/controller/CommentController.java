/*
package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Comment;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

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
        // Recupera a autenticação e obtém o email do usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        // Verifica se o fotógrafo está suspenso de comentar
        if (loggedPhotographer.isCommentSuspended()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Você está suspenso de comentar.");
            return "redirect:/photo/post?photoId=" + photoId;
        }

        // Recupera a foto
        Photo photo = photoService.getPhotoById(photoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        // Se o fotógrafo for o dono da foto, não permite comentar
        if (loggedPhotographer.getId() == photo.getPhotographer().getId()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Você não pode comentar em suas próprias fotos.");
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        Optional<Comment> optionalComment = commentService.getCommentById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            int photoId = comment.getPhoto().getId();
            // Se o novo texto estiver vazio, exclui o comentário
            if (newText.isBlank()) {
                boolean deleted = commentService.deleteComment(id, loggedPhotographer);
                if (deleted) {
                    redirectAttributes.addFlashAttribute("successMessage", "Comentário removido por estar vazio.");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover comentário.");
                }
                return "redirect:/photo/post?photoId=" + photoId;
            }
            boolean updated = commentService.updateComment(id, loggedPhotographer, newText);
            if (updated) {
                redirectAttributes.addFlashAttribute("successMessage", "Comentário atualizado com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar comentário.");
            }
            return "redirect:/photo/post?photoId=" + photoId;
        }
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Integer commentId,
                                RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        Optional<Comment> optionalComment = commentService.getCommentById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            int photoId = comment.getPhoto().getId();
            boolean deleted = commentService.deleteComment(commentId, loggedPhotographer);
            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "Comentário excluído com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir comentário.");
            }
            return "redirect:/photo/post?photoId=" + photoId;
        }
        return "redirect:/home";
    }
}
*/
