package ua.vspelykh.frontmicroservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class FrontController {

    @GetMapping("/home")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/sign-up")
    String signUp() {
        return "sign-up";
    }


    @GetMapping
    public String test() {
        return "template";
    }

}
