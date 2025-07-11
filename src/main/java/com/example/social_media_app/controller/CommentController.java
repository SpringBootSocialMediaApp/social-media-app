package com.example.social_media_app.controller;

import com.example.social_media_app.model.Comment;
import com.example.social_media_app.model.Post;
import com.example.social_media_app.model.User;
import com.example.social_media_app.service.CommentService;
import com.example.social_media_app.service.PostService;
import com.example.social_media_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable Long postId) {
        try {
            List<Comment> comments = commentService.getCommentsByPostId(postId);
            Post post = postService.findById(postId);
            List<Comment> comments1 = commentService.findByPost(post);
          return ResponseEntity.ok(comments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

@PostMapping("/{postId}")
    public ResponseEntity<Comment> createComment(
            @PathVariable Long postId,
            @RequestBody Map<String, String> payload
            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
                    User user1 = userService.findByEmail(userDetails.getUsername());

            Post post = postService.findById(postId);
            User user = userService.findByUsername(username);
            String content = payload.get("content");

            Comment comment = Comment.builder()
                    .content(content)
                    .post(post)
                    .user(user)
                    .build();
            
            Comment savedComment = commentService.save(comment);
            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    //    @PostMapping("/{postId}")
    // public ResponseEntity<Comment> addComment(
    //         @PathVariable Long postId,
    //         @RequestBody Map<String, String> payload,
    //         @AuthenticationPrincipal UserDetails userDetails) {
    //     User user = userService.findByEmail(userDetails.getUsername());
    //     Post post = postService.findById(postId);
        
    //     String content = payload.get("content");

    //     Comment comment = commentService.createComment(content, post, user);
    //     return ResponseEntity.ok(comment);
    // }
}