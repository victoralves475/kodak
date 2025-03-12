package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Hashtag;
import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.service.HashtagService;
import br.edu.ifpb.kodak.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/tag")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;
    private final PhotoService photoService;

    // Método já existente preservado
    @GetMapping("/search")
    @ResponseBody
    public List<String> searchPhotographers(@RequestParam String name) {
        Optional<Hashtag> hashtagOptional = hashtagService.getHashtagByTagName(name);
        return hashtagOptional.map(hashtag -> List.of(hashtag.getTagName()))
                .orElseGet(ArrayList::new);
    }

    // Adiciona hashtag via AJAX
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addHashtagToPhoto(@RequestBody Map<String, String> payload) {
        try {
            int photoId = Integer.parseInt(payload.get("photoId"));
            String tagName = payload.get("tag").trim();

            Photo photo = photoService.getPhotoById(photoId)
                    .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

            Hashtag hashtag = hashtagService.findOrCreateByName(tagName);
            photo.getTags().add(hashtag);
            photoService.savePhoto(photo);

            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }


    // Remove hashtag via AJAX
    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<?> removeHashtag(@RequestBody Map<String, Integer> payload) {
        try {
            Integer photoId = payload.get("photoId");
            Integer tagId = payload.get("tagId");

            if (photoId == null || tagId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "error", "Dados inválidos"));
            }

            hashtagService.removeHashtagFromPhoto(photoId, tagId);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }


    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteHashtag(@RequestBody Map<String, Integer> payload) {
        Integer photoId = payload.get("photoId");
        Integer tagId = payload.get("tagId");

        if (photoId == null || tagId == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Dados inválidos."));
        }

        try {
            hashtagService.removeHashtagFromPhoto(photoId, tagId);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Erro ao excluir hashtag."));
        }
    }

    @GetMapping("/searchTags")
    @ResponseBody
    public List<String> searchHashTags(@RequestParam String name) {
        return hashtagService.searchTagsByPartialName(name);
    }



}