package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.DTO.LoginRequestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class AuthController {

    @GetMapping("/sign-in")
    public ModelAndView showSignInForm() {
        ModelAndView modelAndView = new ModelAndView("login/sign-in");
        modelAndView.addObject("loginRequest", new LoginRequestDTO());
        return modelAndView;
    }
}
