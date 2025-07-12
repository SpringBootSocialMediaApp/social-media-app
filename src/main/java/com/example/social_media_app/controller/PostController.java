package com.example.social_media_app.controller;

import com.example.social_media_app.model.Post;
import com.example.social_media_app.model.User;
import com.example.social_media_app.service.PostService;
import com.example.social_media_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/api/posts/{postId}")
    @ResponseBody
    public ResponseEntity<?> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            // Get the authenticated user
            User currentUser = userService.findByEmail(userDetails.getUsername());

            if (currentUser == null) {
                return ResponseEntity.status(403).body("User not authenticated");
            }

            // Find the post
            Post post = postService.findById(postId);
            if (post == null) {
                return ResponseEntity.notFound().build();
            }

            // Check if the current user is the owner of the post
            if (!post.getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(403).body("You can only delete your own posts");
            }

            // Delete the post
            postService.deletePost(post);

            return ResponseEntity.ok().body("Post deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error deleting post");
        }
    }
}