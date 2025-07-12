package com.example.social_media_app.repository;

import com.example.social_media_app.model.Post;
import com.example.social_media_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    Optional<Post> findById(Long id);
    
    // Count methods for sidebar stats
    long countByUser(User user);
    long countByUserId(Long userId);

    // Friend Integration: Get posts from user and their friends, sorted by timestamp (newest first)
    @Query("SELECT p FROM Post p WHERE " +
           "p.user.id = :userId OR " +
           "p.user.id IN (" +
           "SELECT CASE WHEN f.user1.id = :userId THEN f.user2.id ELSE f.user1.id END " +
           "FROM Friendship f WHERE f.user1.id = :userId OR f.user2.id = :userId" +
           ") " +
           "ORDER BY p.createdAt DESC")
    List<Post> findFeedPostsForUser(@Param("userId") Long userId);
}