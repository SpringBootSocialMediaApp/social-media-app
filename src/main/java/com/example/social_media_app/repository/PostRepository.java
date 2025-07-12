package com.example.social_media_app.repository;

import com.example.social_media_app.model.Post;
import com.example.social_media_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    Optional<Post> findById(Long id);
    
    // Count methods for sidebar stats
    long countByUser(User user);
    long countByUserId(Long userId);
}