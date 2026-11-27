package com.blocksquare.conference.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String daks(Model model) {
        model.addAttribute("pageType", "daks");
        model.addAttribute("pageTitle", "Digital Asset Korea Summit");

        return "user/daks/index";
    }

    @GetMapping({"/aixsoftwave", "/aixsoftwave/"})
    public String aixsoftwave(Model model) {
        model.addAttribute("pageType", "aixsoftwave");
        model.addAttribute("pageTitle", "AI x SOFTWAVE");

        return "user/aixsoftwave/index";
    }
}
