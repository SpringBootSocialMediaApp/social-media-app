package com.example.social_media_app.controller;

import com.example.social_media_app.dto.UserStatsDto;
import com.example.social_media_app.model.User;
import com.example.social_media_app.service.PostService;
import com.example.social_media_app.service.UserService;
import com.example.social_media_app.service.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;
    private final PostService postService;
    private final UserStatsService userStatsService;

    @Autowired
    public HomeController(UserService userService, PostService postService, UserStatsService userStatsService) {
        this.userService = userService;
        this.postService = postService;
        this.userStatsService = userStatsService;
    }

    @GetMapping("/profile-settings")
    public String profileSettings() {
        return "components/profileSetings"; // no .html extension
    }

    @GetMapping("/friends")
    public String friends() {
        return "friends"; // resolves to src/main/resources/templates/friends.html
    }


    @GetMapping({"/", "/home"})
    public String home(
            @AuthenticationPrincipal UserDetails currentUserDetails,
            Model model
    ) {
        // lookup your JPA User by the logged-in username
        User user = userService.findByEmail(currentUserDetails.getUsername());
        
        // Get user stats
        UserStatsDto userStats = userStatsService.getUserStats(user.getId());
        
        // put it into the Thymeleaf model
        model.addAttribute("currentUser", user);
        model.addAttribute("user", user);
        model.addAttribute("userStats", userStats);
        model.addAttribute("posts", postService.getAllPosts());
        // render templates/home.html
        return "home";
    }

}
