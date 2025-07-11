package com.example.social_media_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class HomeController {
//    @GetMapping("/home")
//    public String home() {
//        return "home"; // returns home.html
//    }
//
//
//}

import com.example.social_media_app.model.User;
import com.example.social_media_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // logged-in user's email
        User user = userService.findByEmail(email);
        System.out.println("Logged in user email: " + email);
        System.out.println("User fetched: " + (user != null ? user.getFirstName() : "null"));
        model.addAttribute("user", user); // send user to view
        return "home"; // home.html
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser) {
        userService.updateUser(updatedUser);
        return "redirect:/home"; // refresh profile
    }
}

