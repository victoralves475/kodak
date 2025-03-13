package br.edu.ifpb.kodak.controller;

import br.edu.ifpb.kodak.model.Hashtag;
import br.edu.ifpb.kodak.service.HashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tag")
public class HashtagController {

    @Autowired
    private HashtagService hashtagService;

    @GetMapping("/search")
    @ResponseBody
    public List<String> searchTags(@RequestParam String name) {
        List<Hashtag> hashtags = hashtagService.getHashtagByTagName(name);
        List<String> tagNames = new ArrayList<>();

        for (Hashtag hashtag : hashtags) {
            tagNames.add(hashtag.getTagName());
        }

        return tagNames;
    }

}
