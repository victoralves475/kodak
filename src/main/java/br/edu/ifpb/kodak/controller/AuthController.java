package br.edu.ifpb.kodak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.model.DTO.LoginRequestDTO;
import br.edu.ifpb.kodak.service.AuthService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/sign-in")
    public ModelAndView showSignInForm() {
        ModelAndView modelAndView = new ModelAndView("login/sign-in");
        modelAndView.addObject("loginRequest", new LoginRequestDTO());
        return modelAndView;
    }

    @PostMapping("/sign-in")
    public ModelAndView signIn(
            @Valid LoginRequestDTO loginRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("login/sign-in");
            modelAndView.addObject("loginRequest", loginRequest);
            return modelAndView;
        }

        // Autenticar e obter o fotógrafo
        Photographer photographer = authService.authenticateAndGet(loginRequest);

        if (photographer != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Login realizado com sucesso!");
            modelAndView.setViewName("redirect:/photographer/home?photographerId=" + photographer.getId());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Email ou senha inválidos.");
            modelAndView.setViewName("redirect:/login/sign-in");
        }

        return modelAndView;
    }

}
