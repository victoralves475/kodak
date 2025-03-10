package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Comment;
import br.edu.ifpb.kodak.service.CommentService;
import br.edu.ifpb.kodak.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<byte[]> downloadPdf(@RequestParam int photo_id) {

        List<Comment> comments = commentService.getCommentsByPhotoId(photo_id);

        return pdfService.generatePdf(comments);
    }
}
