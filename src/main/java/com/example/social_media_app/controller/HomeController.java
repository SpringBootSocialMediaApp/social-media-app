package com.example.social_media_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home() {
        return "home"; // returns home.html
    }

    @GetMapping("/friends")
    public String friends() {
        return "friends"; // resolves to src/main/resources/templates/friends.html
    }


}
