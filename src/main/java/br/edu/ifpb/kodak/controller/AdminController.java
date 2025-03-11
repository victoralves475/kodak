package br.edu.ifpb.kodak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotographerService;
import br.edu.ifpb.kodak.service.security.UsuarioService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Lista os fotógrafos para administração com paginação.
     */
    @GetMapping()
    public String listPhotographers(
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue="2") int size,
            Model model, RedirectAttributes redirectAttributes) {
        // Recupera a autenticação via SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer loggedPhotographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Fotógrafo logado não encontrado"));

        // Verifica se o usuário possui a role "ADMIN"
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            redirectAttributes.addFlashAttribute("errorMessage", "Você não tem permissão para acessar esta página.");
            return "redirect:/photographer/home?photographerId=" + loggedPhotographer.getId();
        }

        Page<Photographer> photographerPage = photographerService.getPhotographers(PageRequest.of(page, size));
        model.addAttribute("photographerPage", photographerPage);
        return "admin/photographers";
    }

    /**
     * Atualiza, de forma unificada, os status de suspensão e de admin dos fotógrafos.
     * Espera dois parâmetros: uma lista de IDs para suspensão (suspendIds) e outra para status admin (adminIds).
     */
    @PostMapping("/photographers/update")
    public String updatePhotographers(
            @RequestParam(name="suspendIds", required=false) List<Integer> suspendIds,
            @RequestParam(name="adminIds", required=false) List<Integer> adminIds,
            RedirectAttributes redirectAttributes) {

        List<Photographer> allPhotographers = photographerService.getAllPhotographers();

        for (Photographer photographer : allPhotographers) {
            // Atualiza o status de suspensão
            boolean shouldSuspend = (suspendIds != null && suspendIds.contains(photographer.getId()));
            photographer.setSuspended(shouldSuspend);

            // Atualiza o status de admin: se o ID estiver em adminIds, promove; caso contrário, demove
            if (adminIds != null && adminIds.contains(photographer.getId())) {
                photographer.setAdmin(true);
                usuarioService.promoteToAdmin(photographer.getEmail());
            } else {
                photographer.setAdmin(false);
                usuarioService.demoteFromAdmin(photographer.getEmail());
            }
            photographerService.savePhotographer(photographer);
        }

        redirectAttributes.addFlashAttribute("message", "As alterações foram salvas com sucesso.");
        return "redirect:/admin";
    }
}
