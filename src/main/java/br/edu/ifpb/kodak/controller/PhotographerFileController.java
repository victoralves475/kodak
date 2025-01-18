package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.service.FileStorageService;
import br.edu.ifpb.kodak.repository.PhotographerRepository;
import br.edu.ifpb.kodak.model.Photographer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class PhotographerFileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PhotographerRepository photographerRepository;

    @PostMapping("/photographer/update-profile-pic")
    public String updateProfilePic(MultipartFile profilePic, Integer photographerId, Model model) {
        try {
            // Salva a foto de perfil
            String profilePicPath = fileStorageService.saveProfilePic(profilePic, photographerId);

            // Atualiza o caminho no banco de dados
            Photographer photographer = photographerRepository.findById(photographerId)
                    .orElseThrow(() -> new RuntimeException("Fotógrafo não encontrado"));

            photographer.setProfilePicPath(profilePicPath);
            photographerRepository.save(photographer);

            model.addAttribute("successMessage", "Foto de perfil atualizada com sucesso!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Erro ao salvar a foto de perfil.");
        }

        return "redirect:/photographer/home";
    }
}
