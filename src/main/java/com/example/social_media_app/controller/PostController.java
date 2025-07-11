package com.example.social_media_app.controller;

import com.example.social_media_app.dto.post.CommentCreateDto;
import com.example.social_media_app.dto.post.CommentDto;
import com.example.social_media_app.dto.post.PostCreateDto;
import com.example.social_media_app.dto.post.PostResponseDto;
import com.example.social_media_app.model.User;
import com.example.social_media_app.security.CustomUserDetails;
import com.example.social_media_app.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // REST API Endpoints

    @PostMapping("/api/posts")
    @ResponseBody
    public ResponseEntity<PostResponseDto> createPost(
            @Valid @RequestBody PostCreateDto postCreateDto,
            @AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPost(postCreateDto, currentUserDetails.getUser()));
    }

    @GetMapping("/api/posts")
    @ResponseBody
    public ResponseEntity<List<PostResponseDto>> getAllPosts(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(postService.getAllPosts(currentUser));
    }

    @GetMapping("/api/posts/paged")
    @ResponseBody
    public ResponseEntity<Page<PostResponseDto>> getAllPostsPaginated(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(postService.getAllPostsPaginated(currentUser, pageable));
    }

    @GetMapping("/api/posts/{postId}")
    @ResponseBody
    public ResponseEntity<PostResponseDto> getPostById(
            @PathVariable Long postId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(postService.getPostById(postId, currentUser));
    }

    @PutMapping("/api/posts/{postId}")
    @ResponseBody
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostCreateDto postCreateDto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(postService.updatePost(postId, postCreateDto, currentUser));
    }

    @DeleteMapping("/api/posts/{postId}")
    @ResponseBody
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal User currentUser) {
        postService.deletePost(postId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/users/{userId}/posts")
    @ResponseBody
    public ResponseEntity<List<PostResponseDto>> getUserPosts(
            @PathVariable Long userId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(postService.getPostsByUser(userId, currentUser));
    }

    @GetMapping("/api/users/{userId}/posts/paged")
    @ResponseBody
    public ResponseEntity<Page<PostResponseDto>> getUserPostsPaginated(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(postService.getPostsByUserPaginated(userId, currentUser, pageable));
    }

    @PostMapping("/api/posts/{postId}/comments")
    @ResponseBody
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateDto commentCreateDto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.addComment(postId, commentCreateDto, currentUser));
    }

    @GetMapping("/api/posts/{postId}/comments")
    @ResponseBody
    public ResponseEntity<List<CommentDto>> getPostComments(
            @PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostComments(postId));
    }

    @GetMapping("/api/posts/{postId}/comments/paged")
    @ResponseBody
    public ResponseEntity<Page<CommentDto>> getPostCommentsPaginated(
            @PathVariable Long postId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(postService.getPostCommentsPaginated(postId, pageable));
    }

    @PutMapping("/api/comments/{commentId}")
    @ResponseBody
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentCreateDto commentCreateDto,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(postService.updateComment(commentId, commentCreateDto, currentUser));
    }

    @DeleteMapping("/api/comments/{commentId}")
    @ResponseBody
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User currentUser) {
        postService.deleteComment(commentId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/posts/{postId}/like")
    @ResponseBody
    public ResponseEntity<PostResponseDto> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(postService.toggleLike(postId, currentUser));
    }

    @GetMapping("/api/posts/{postId}/like")
    @ResponseBody
    public ResponseEntity<Boolean> isPostLikedByUser(
            @PathVariable Long postId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(postService.isLikedByUser(postId, currentUser));
    }

    // MVC Views

    @GetMapping("/posts")
    public String getPostsPage(Model model, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("posts", postService.getAllPosts(currentUser));
        return "posts";
    }

    @GetMapping("/posts/{postId}")
    public String getPostDetailsPage(@PathVariable Long postId, Model model,
            @AuthenticationPrincipal User currentUser) {
        model.addAttribute("post", postService.getPostById(postId, currentUser));
        model.addAttribute("comments", postService.getPostComments(postId));
        return "post-details";
    }
}
