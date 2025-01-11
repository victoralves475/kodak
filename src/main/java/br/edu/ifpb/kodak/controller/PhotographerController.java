package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView savePhotographer(Photographer photographer, ModelAndView model, RedirectAttributes attr){
        photographerService.savePhotographer(photographer);
        attr.addFlashAttribute("mensagem", "Fotógrafo " + photographer.getName() + " " + "salvo" + "com sucesso!");
        model.setViewName("redirect:/photographer/form");
        return model;
    }
}
