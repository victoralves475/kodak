package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotoService;
import br.edu.ifpb.kodak.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotographerService photographerService;

    // Endpoint que renderiza a view da publicação
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

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("loggedPhotographer", loggedPhotographer);

        return "photo/post";
    }

    /**
     * Endpoint para curtir (ou "toggle" like) uma foto via AJAX.
     * Este método retorna JSON com a nova contagem de curtidas.
     */
    @PostMapping("/like")
    @ResponseBody
    public ResponseEntity<?> likePhotoAjax(@RequestBody Map<String, String> payload, Authentication authentication) {
        // Extrai o photoId do payload JSON
        Integer photoId = Integer.valueOf(payload.get("photoId"));

        // Recupera a foto
        Photo photo = photoService.getPhotoById(photoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        // Recupera o fotógrafo logado via Authentication
        String email = authentication.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        // Se o fotógrafo for o dono da foto, não permite curtir
        if (loggedPhotographer.getId() == photo.getPhotographer().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "error", "Você não pode curtir a própria foto"));
        }

        // Chama o método para adicionar (ou remover) a curtida
        photographerService.likePhoto(photoId, loggedPhotographer);

        // Atualiza a contagem de curtidas – note que pode ser necessário recarregar a foto do banco para obter a contagem atualizada
        int likesCount = photo.getLikedPhotographers().size();
        return ResponseEntity.ok(Map.of("success", true, "likesCount", likesCount));
    }
}
