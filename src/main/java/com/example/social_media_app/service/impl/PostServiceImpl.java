package com.example.social_media_app.service.impl;

import com.example.social_media_app.dto.post.CommentCreateDto;
import com.example.social_media_app.dto.post.CommentDto;
import com.example.social_media_app.dto.post.PostCreateDto;
import com.example.social_media_app.dto.post.PostResponseDto;
import com.example.social_media_app.model.Comment;
import com.example.social_media_app.model.Post;
import com.example.social_media_app.model.PostLike;
import com.example.social_media_app.model.User;
import com.example.social_media_app.repository.CommentRepository;
import com.example.social_media_app.repository.PostLikeRepository;
import com.example.social_media_app.repository.PostRepository;
import com.example.social_media_app.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    @Transactional
    public PostResponseDto createPost(PostCreateDto postCreateDto, User currentUser) {
        Post post = Post.builder()
                .content(postCreateDto.getContent())
                .user(currentUser)
                .likeCount(0)
                .commentCount(0)
                .build();

        Post savedPost = postRepository.save(post);
        return mapPostToResponseDto(savedPost, currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts(User currentUser) {
        List<Post> posts = postRepository.findAllPostsOrderByCreatedAtDesc();
        return posts.stream()
                .map(post -> mapPostToResponseDto(post, currentUser))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllPostsPaginated(User currentUser, Pageable pageable) {
        Page<Post> postPage = postRepository.findAllPostsOrderByCreatedAtDesc(pageable);
        return postPage.map(post -> mapPostToResponseDto(post, currentUser));
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));
        return mapPostToResponseDto(post, currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByUser(Long userId, User currentUser) {
        List<Post> posts = postRepository.findAllPostsByUserIdOrderByCreatedAtDesc(userId);
        return posts.stream()
                .map(post -> mapPostToResponseDto(post, currentUser))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPostsByUserPaginated(Long userId, User currentUser, Pageable pageable) {
        Page<Post> postPage = postRepository.findAllPostsByUserIdOrderByCreatedAtDesc(userId, pageable);
        return postPage.map(post -> mapPostToResponseDto(post, currentUser));
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long postId, PostCreateDto postCreateDto, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You don't have permission to update this post");
        }

        post.setContent(postCreateDto.getContent());
        Post updatedPost = postRepository.save(post);
        return mapPostToResponseDto(updatedPost, currentUser);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this post");
        }

        postRepository.delete(post);
    }

    @Override
    @Transactional
    public CommentDto addComment(Long postId, CommentCreateDto commentCreateDto, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

        Comment comment = Comment.builder()
                .content(commentCreateDto.getContent())
                .post(post)
                .user(currentUser)
                .build();

        Comment savedComment = commentRepository.save(comment);

        // Update comment count
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        return mapCommentToDto(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getPostComments(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream()
                .map(this::mapCommentToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentDto> getPostCommentsPaginated(Long postId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId, pageable);
        return commentPage.map(this::mapCommentToDto);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long commentId, CommentCreateDto commentCreateDto, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + commentId));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You don't have permission to update this comment");
        }

        comment.setContent(commentCreateDto.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return mapCommentToDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + commentId));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this comment");
        }

        Post post = comment.getPost();
        post.setCommentCount(post.getCommentCount() - 1);
        postRepository.save(post);

        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public PostResponseDto toggleLike(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

        boolean isLiked = postLikeRepository.existsByPostIdAndUserId(postId, currentUser.getId());

        if (isLiked) {
            // Unlike the post
            postLikeRepository.deleteByPostIdAndUserId(postId, currentUser.getId());
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            // Like the post
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .user(currentUser)
                    .build();
            postLikeRepository.save(postLike);
            post.setLikeCount(post.getLikeCount() + 1);
        }

        Post updatedPost = postRepository.save(post);
        return mapPostToResponseDto(updatedPost, currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLikedByUser(Long postId, User currentUser) {
        return postLikeRepository.existsByPostIdAndUserId(postId, currentUser.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getLikeCount(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }

    // Helper methods for mapping entities to DTOs
    private PostResponseDto mapPostToResponseDto(Post post, User currentUser) {
        boolean isLikedByCurrentUser = false;
        if (currentUser != null) {
            isLikedByCurrentUser = postLikeRepository.existsByPostIdAndUserId(post.getId(), currentUser.getId());
        }

        PostResponseDto.UserInfoDto userInfo = PostResponseDto.UserInfoDto.builder()
                .id(post.getUser().getId())
                .firstName(post.getUser().getFirstName())
                .lastName(post.getUser().getLastName())
                .username(post.getUser().getUsername() != null ? post.getUser().getUsername()
                        : post.getUser().getEmail().split("@")[0])
                .profilePicture(post.getUser().getProfilePicture())
                .build();

        return PostResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .user(userInfo)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .likedByCurrentUser(isLikedByCurrentUser)
                .comments(new ArrayList<>())
                .build();
    }

    private CommentDto mapCommentToDto(Comment comment) {
        PostResponseDto.UserInfoDto userInfo = PostResponseDto.UserInfoDto.builder()
                .id(comment.getUser().getId())
                .firstName(comment.getUser().getFirstName())
                .lastName(comment.getUser().getLastName())
                .username(comment.getUser().getUsername() != null ? comment.getUser().getUsername()
                        : comment.getUser().getEmail().split("@")[0])
                .profilePicture(comment.getUser().getProfilePicture())
                .build();

        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(userInfo)
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
