package com.example.social_media_app.service.impl;

import com.example.social_media_app.model.Like;
import com.example.social_media_app.model.Post;
import com.example.social_media_app.model.User;
import com.example.social_media_app.repository.LikeRepository;
import com.example.social_media_app.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public boolean toggleLike(Post post, User user) {
        Optional<Like> existingLike = likeRepository.findByPostAndUser(post, user);
        
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false;
        } else {
            Like newLike = new Like();
            newLike.setPost(post);
            newLike.setUser(user);
            likeRepository.save(newLike);
            return true;
        }
    }

    @Override
    public boolean isLiked(Post post, User user) {
        return likeRepository.findByPostAndUser(post, user).isPresent();
    }

    @Override
    public int getLikeCount(Post post) {
        return likeRepository.countByPost(post);
    }
}