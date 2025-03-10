package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.service.HashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tag")
public class HashtagController {

    @Autowired
    private HashtagService hashtagService;

}
