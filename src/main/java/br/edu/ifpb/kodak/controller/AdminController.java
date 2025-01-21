package br.edu.ifpb.kodak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotographerService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PhotographerService photographerService;

    /**
     * Listagem de fotógrafos para administração.
     */
    @GetMapping()
    public String listPhotographers(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Photographer loggedPhotographer = (Photographer) session.getAttribute("loggedPhotographer");

        if (!loggedPhotographer.isAdmin()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Você não tem permissão para acessar esta página.");
            return "redirect:/photographer/home?photographerId=" + loggedPhotographer.getId();
        }

        List<Photographer> photographers = photographerService.getAllPhotographers();
        model.addAttribute("photographers", photographers);
        return "admin/photographers"; // Caminho do template em templates/admin/photographers.html
    }

    /**
     * Atualiza o status de suspensão dos fotógrafos selecionados.
     */
    @PostMapping("/photographers/suspend")
    public String suspendPhotographers(@RequestParam(name = "photographerIds", required = false) List<Integer> photographerIds,
                                       RedirectAttributes redirectAttributes, HttpSession session) {
        List<Photographer> allPhotographers = photographerService.getAllPhotographers();

        for (Photographer photographer : allPhotographers) {
            // Verifica se o fotógrafo está na lista de IDs selecionados
            boolean shouldSuspend = photographerIds != null && photographerIds.contains(photographer.getId());
            photographer.setSuspended(shouldSuspend);
            photographerService.savePhotographer(photographer);
        }

        // Adiciona mensagem de sucesso
        redirectAttributes.addFlashAttribute("message", "As alterações foram salvas com sucesso.");
        return "redirect:/admin/photographers"; // Redireciona para a página de listagem
    }
}
