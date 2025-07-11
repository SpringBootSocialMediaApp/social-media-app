package com.example.social_media_app.controller;

import com.example.social_media_app.model.User;
import com.example.social_media_app.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.time.Period;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {
        User user = userRepository.findAll().stream().findFirst()
                .orElse(new User()); // Return empty user if none exists

        // Calculate age if date of birth exists
        if (user.getDateOfBirth() != null) {
            int age = Period.between(user.getDateOfBirth(), LocalDate.now()).getYears();
            model.addAttribute("userAge", age);
        }

        model.addAttribute("user", user);
        return "home";
    }
}