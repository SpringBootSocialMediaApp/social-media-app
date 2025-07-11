package com.example.social_media_app.service;

import com.example.social_media_app.dto.post.CommentCreateDto;
import com.example.social_media_app.dto.post.CommentDto;
import com.example.social_media_app.dto.post.PostCreateDto;
import com.example.social_media_app.dto.post.PostResponseDto;
import com.example.social_media_app.model.Comment;
import com.example.social_media_app.model.Post;
import com.example.social_media_app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    // Post operations
    PostResponseDto createPost(PostCreateDto postCreateDto, User currentUser);

    List<PostResponseDto> getAllPosts(User currentUser);

    Page<PostResponseDto> getAllPostsPaginated(User currentUser, Pageable pageable);

    PostResponseDto getPostById(Long postId, User currentUser);

    List<PostResponseDto> getPostsByUser(Long userId, User currentUser);

    Page<PostResponseDto> getPostsByUserPaginated(Long userId, User currentUser, Pageable pageable);

    PostResponseDto updatePost(Long postId, PostCreateDto postCreateDto, User currentUser);

    void deletePost(Long postId, User currentUser);

    // Comment operations
    CommentDto addComment(Long postId, CommentCreateDto commentCreateDto, User currentUser);

    List<CommentDto> getPostComments(Long postId);

    Page<CommentDto> getPostCommentsPaginated(Long postId, Pageable pageable);

    CommentDto updateComment(Long commentId, CommentCreateDto commentCreateDto, User currentUser);

    void deleteComment(Long commentId, User currentUser);

    // Like operations
    PostResponseDto toggleLike(Long postId, User currentUser);

    boolean isLikedByUser(Long postId, User currentUser);

    Long getLikeCount(Long postId);
}
