package com.example.social_media_app.repository;

import com.example.social_media_app.model.Like;
import com.example.social_media_app.model.Post;
import com.example.social_media_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
    int countByPost(Post post);
}