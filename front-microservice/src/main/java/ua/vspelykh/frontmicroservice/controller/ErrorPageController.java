package ua.vspelykh.frontmicroservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class ErrorPageController {

    @GetMapping("/forbidden")
    public String forbidden(){
        return "errors/403";
    }
}
