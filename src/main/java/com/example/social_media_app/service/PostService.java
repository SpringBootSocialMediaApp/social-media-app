package com.example.social_media_app.service;

import com.example.social_media_app.model.Post;
import com.example.social_media_app.model.User;
import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    
    Post createPost(String content, User author);
    Post findById(Long id);
}