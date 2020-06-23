package com.example.blocks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SinglePageController {
    // @GetMapping("/**/{path:[^.]*}")
    @GetMapping("/react")
    public String any() {
        return "forward:/index.html";
    }
}
