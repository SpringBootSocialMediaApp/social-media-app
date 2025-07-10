package com.example.social_media_app.controller;

import com.example.social_media_app.model.User;
import com.example.social_media_app.service.PostService;
import com.example.social_media_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping("/posts")
    public String createPost(
            @RequestParam("content") String content,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User author = userService.findByEmail(userDetails.getUsername());
        postService.createPost(content, author);
        return "redirect:/home";
    }
}
