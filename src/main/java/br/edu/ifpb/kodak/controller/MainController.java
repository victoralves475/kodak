package br.edu.ifpb.kodak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotographerService;

@Controller
public class MainController {

    @Autowired
    private PhotographerService photographerService;

    @GetMapping("/")
    public String redirectToHome() {
        // Obtém a autenticação atual a partir do SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se há um usuário autenticado (não é anônimo)
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            Photographer photographer = photographerService.getPhotographerByEmail(email)
                    .orElse(null);
            if (photographer != null) {
                return "redirect:/photographer/home?photographerId=" + photographer.getId();
            }
        }

        return "redirect:/login/sign-in";
    }
}
