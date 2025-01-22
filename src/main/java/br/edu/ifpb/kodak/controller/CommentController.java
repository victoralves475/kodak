package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Photo;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.kodak.model.Comment;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.repository.PhotoRepository;
import br.edu.ifpb.kodak.service.CommentService;
import br.edu.ifpb.kodak.service.PhotoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PhotoService photoService;

    @PostMapping("/new")
    public String newComment(@RequestParam("photoId") Integer photoId,
                             @RequestParam("commentText") String commentText, Model model, HttpSession session) {
        Photographer loggedPhotographer = (Photographer) session.getAttribute("loggedPhotographer");

        Photo photo = photoService.getPhotoById(photoId).orElse(null);

        if (loggedPhotographer.getId() == photo.getPhotographer().getId()) {
            model.addAttribute("errorMessage", "Você não pode comentar em suas próprias fotos.");
            return "redirect:/photo/post?photoId=" + photoId;
        }

        commentService.addCommentToPhoto(commentText, photo, loggedPhotographer);
        
        return "redirect:/photo/post?photoId=" + photoId;
    }
}