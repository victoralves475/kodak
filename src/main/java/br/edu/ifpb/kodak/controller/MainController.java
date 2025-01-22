package br.edu.ifpb.kodak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.ifpb.kodak.model.Photographer;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @GetMapping("/")
    public String redirectToLogin(HttpSession session) {
        Photographer loggedPhotographer = (Photographer) session.getAttribute("loggedPhotographer");

        if (loggedPhotographer != null) {
            return "redirect:/photographer/home?photographerId=" + loggedPhotographer.getId(); // Redireciona para a página do Fotógrafo
        }

        return "redirect:/login/sign-in"; // Redireciona para a página de login
    }
}
