package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Comment;
import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.CommentService;
import br.edu.ifpb.kodak.service.PhotoService;
import br.edu.ifpb.kodak.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/comment")
public class CommentRestController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotographerService photographerService;

    /**
     * Cria um novo comentário via AJAX.
     * Retorna JSON com sucesso ou erro.
     */
    @PostMapping("/new")
    public ResponseEntity<?> newCommentAjax(@RequestBody Map<String, String> payload,
                                            Authentication authentication) {
        Integer photoId = Integer.valueOf(payload.get("photoId"));
        String commentText = payload.get("commentText");

        String email = authentication.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        Photo photo = photoService.getPhotoById(photoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        if (loggedPhotographer.isCommentSuspended() && !photo.getPhotographer().equals(loggedPhotographer)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "error", "Sua conta está suspensa para comentar."));
        }

//        if (loggedPhotographer.getId() == photo.getPhotographer().getId()) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Map.of("success", false, "error", "Você não pode comentar em suas próprias fotos."));
//        }

        Comment comment = commentService.addCommentToPhoto(commentText, photo, loggedPhotographer);

        // Garante que nenhum valor seja nulo
        String createdAtStr = comment.getCreatedAt() != null ? comment.getCreatedAt().toString() : "";
        String photographerName = (comment.getPhotographer() != null && comment.getPhotographer().getName() != null)
                ? comment.getPhotographer().getName() : "";
        String photographerProfilePic = (comment.getPhotographer() != null && comment.getPhotographer().getProfilePicPath() != null)
                ? comment.getPhotographer().getProfilePicPath() : "";

        return ResponseEntity.ok(Map.of(
                "success", true,
                "commentId", comment.getId(),
                "commentText", comment.getCommentText(),
                "createdAt", createdAtStr,
                "photographerName", photographerName,
                "photographerProfilePic", photographerProfilePic
        ));
    }



    /**
     * Atualiza um comentário via AJAX.
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCommentAjax(@PathVariable Integer id,
                                               @RequestBody Map<String, String> payload,
                                               Authentication authentication) {
        String newText = payload.get("commentText");
        String email = authentication.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        Optional<Comment> existingCommentOpt = commentService.getCommentById(id);
        if (existingCommentOpt.isPresent()) {
            // Se o novo texto estiver vazio, exclui o comentário
            if (newText.trim().isEmpty()) {
                boolean deleted = commentService.deleteComment(id, loggedPhotographer);
                if (deleted) {
                    return ResponseEntity.ok(Map.of("success", true, "deleted", true));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("success", false, "error", "Erro ao remover comentário."));
                }
            }
            Optional<Comment> updatedCommentOpt = commentService.updateComment(id, loggedPhotographer, newText);
            if (updatedCommentOpt.isPresent()) {
                Comment updatedComment = updatedCommentOpt.get();
                return ResponseEntity.ok(Map.of("success", true, "commentText", updatedComment.getCommentText()));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("success", false, "error", "Erro ao atualizar comentário."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", "Comentário não encontrado."));
        }
    }

    /**
     * Exclui um comentário via AJAX.
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCommentAjax(@PathVariable("id") Integer commentId,
                                               Authentication authentication) {
        String email = authentication.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        Optional<Comment> existingCommentOpt = commentService.getCommentById(commentId);
        if (existingCommentOpt.isPresent()) {
            boolean deleted = commentService.deleteComment(commentId, loggedPhotographer);
            if (deleted) {
                return ResponseEntity.ok(Map.of("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("success", false, "error", "Erro ao excluir comentário."));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "error", "Comentário não encontrado."));
    }
}
