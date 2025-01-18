package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotographerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/photographer")
public class PhotographerController {

    @Autowired
    private PhotographerService photographerService;

    @GetMapping("/form")
    public String getForm(Photographer photographer, Model model) {
        model.addAttribute("photographer", photographer);
        return "/photographer/form";
    }

    @PostMapping
    public ModelAndView savePhotographer(@Valid Photographer photographer,  BindingResult result, ModelAndView model, RedirectAttributes attr){
        if (result.hasErrors()) {
            model.setViewName("/photographer/form");
            return model;

        }
        photographerService.savePhotographer(photographer);
        attr.addFlashAttribute("mensagem", "Fotógrafo " + photographer.getName() + " " + "salvo" + "com sucesso!");
        model.setViewName("redirect:/");
        return model;
    }

    @GetMapping("/home")
    public ModelAndView getHomePage( Photographer photographer, ModelAndView model, RedirectAttributes attr) {
        model.setViewName("/photographer/home");
        return model;
    }



}
