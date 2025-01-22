package br.edu.ifpb.kodak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

     @GetMapping("/")
     public String redirectToLogin() {
         return "redirect:/login/sign-in"; // Redireciona para a página de login
     }
}
