package br.edu.ifpb.kodak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        boolean isAuthenticated = authService.authenticate(loginRequest);

        if (isAuthenticated) {
            redirectAttributes.addFlashAttribute("successMessage", "Login realizado com sucesso!");
            modelAndView.setViewName("redirect:/photographer/home");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Email ou senha inválidos.");
            modelAndView.setViewName("redirect:/login/sign-in");
        }

        return modelAndView;
    }
}
