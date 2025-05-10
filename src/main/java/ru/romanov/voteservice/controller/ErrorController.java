package ru.romanov.voteservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error1")
    public String error() {
        return "redirect:/dashboard";
    }
}
