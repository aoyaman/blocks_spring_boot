package com.example.blocks.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

  @RequestMapping("/")
  public String home(Model model, Principal principal) {

    if (principal != null) {
      return "redirect:/game/index";
    }
    model.addAttribute("principal", principal);
    return "home";
  }



}
