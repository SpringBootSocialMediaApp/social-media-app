package com.example.social_media_app.controller;

import com.example.social_media_app.model.User;
import com.example.social_media_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile/settings")
    public String showSettings(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "profileSetings"; // Make sure name matches your HTML file
    }

    @GetMapping("/profile/current")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userService.findByEmail(authentication.getName());

            Map<String, Object> response = new HashMap<>();
            response.put("id", currentUser.getId());
            response.put("firstName", currentUser.getFirstName());
            response.put("lastName", currentUser.getLastName());
            response.put("email", currentUser.getEmail());
            response.put("username", currentUser.getUsername());
            response.put("profilePicture", currentUser.getProfilePicture());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            User existingUser = userService.findByEmail(userDetails.getUsername());

            // Check if email is being changed and if new email already exists
            if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
                if (userService.existsByEmail(updatedUser.getEmail())) {
                    redirectAttributes.addFlashAttribute("error",
                            "Email address is already in use by another account.");
                    return "redirect:/home";
                }
            }

            // Only allow editing certain fields
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());

            // Update username based on email if it's being changed
            if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
                String newUsername = updatedUser.getEmail().split("@")[0];
                existingUser.setUsername(newUsername);
            }

            // Only update password if provided and not empty
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            userService.save(existingUser);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile. Please try again.");
        }

        return "redirect:/home";
    }

    @PostMapping("/profile/update-ajax")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProfileAjax(@RequestBody Map<String, String> updateData) {
        Map<String, Object> response = new HashMap<>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User existingUser = userService.findByEmail(authentication.getName());

            // Check if email is being changed and if new email already exists
            String newEmail = updateData.get("email");
            if (newEmail != null && !existingUser.getEmail().equals(newEmail)) {
                if (userService.existsByEmail(newEmail)) {
                    response.put("success", false);
                    response.put("message", "Email address is already in use by another account.");
                    return ResponseEntity.badRequest().body(response);
                }
            }

            // Update user fields
            if (updateData.get("firstName") != null) {
                existingUser.setFirstName(updateData.get("firstName"));
            }
            if (updateData.get("lastName") != null) {
                existingUser.setLastName(updateData.get("lastName"));
            }
            if (newEmail != null) {
                existingUser.setEmail(newEmail);
                // Update username based on new email
                String newUsername = newEmail.split("@")[0];
                existingUser.setUsername(newUsername);
            }

            // Update password only if provided
            String newPassword = updateData.get("password");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(newPassword));
            }

            userService.save(existingUser);

            // Return updated user data
            response.put("success", true);
            response.put("message", "Profile updated successfully!");
            
            Map<String, Object> userData = Map.of(
                    "id", existingUser.getId(),
                    "firstName", existingUser.getFirstName(),
                    "lastName", existingUser.getLastName(),
                    "email", existingUser.getEmail(),
                    "username", existingUser.getUsername(),
                    "profilePicture", existingUser.getProfilePicture() != null ? existingUser.getProfilePicture() : "");
            
            response.put("user", userData);
            
            System.out.println("=== Profile Update Response ===");
            System.out.println("User data being returned: " + userData);
            System.out.println("Username: " + existingUser.getUsername());
            System.out.println("Email: " + existingUser.getEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update profile. Please try again.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
