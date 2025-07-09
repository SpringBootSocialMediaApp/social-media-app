package com.example.social_media_app.controller;

import com.example.social_media_app.dto.RegisterDto;
import com.example.social_media_app.model.entity.User;
import com.example.social_media_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(
            @ModelAttribute("registerDto") @Valid RegisterDto registerDto,
            BindingResult result,
            Model model) {

        // Check if email already exists
        if (userService.findByEmail(registerDto.getEmail()) != null) {
            result.rejectValue("email", "error.registerDto", "An account already exists for this email.");
        }

        if (result.hasErrors()) {
            return "register";
        }

        // Map DTO to Entity
        User user = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password(registerDto.getPassword()) // Will be encoded in service
                .build();

        userService.registerUser(user);
        model.addAttribute("successMessage", "Registration successful! Please log in.");
        return "redirect:/login";
    }
}
