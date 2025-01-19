package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.FileStorageService;
import br.edu.ifpb.kodak.service.PhotographerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/sign-up")
public class SignUpController {
    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public String getForm(Photographer photographer, Model model) {
        model.addAttribute("photographer", photographer);
        return "/sign-up/form";
    }

    @PostMapping
    public ModelAndView savePhotographer(
            @Valid Photographer photographer, BindingResult result,
            @RequestParam("foto") MultipartFile photo,
            ModelAndView model,
            RedirectAttributes attr) throws IOException
    {
        if (result.hasErrors()) {
            model.setViewName("/sign-up/form");
            System.out.println(result.getAllErrors());
            return model;
        }

        if (photographerService.existsPhotographerByEmail(photographer.getEmail())) {
            result.rejectValue("email", "error.photographer", "Já existe um fotógrafo cadastrado com este email.");
            model.setViewName("/sign-up/form");
            return model;
        }

        photographerService.savePhotographer(photographer);

        if(!photo.isEmpty()){
            String filePath = fileStorageService.saveProfilePic(photo, photographer.getId());
            photographer.setProfilePicPath(filePath);
            photographerService.savePhotographer(photographer);
        }

        attr.addFlashAttribute("mensagem", "Fotógrafo " + photographer.getName() + " " + "salvo" + "com sucesso!");
        model.setViewName("redirect:/login/sign-in");
        return model;
    }
}
