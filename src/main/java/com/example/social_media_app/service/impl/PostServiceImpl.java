package com.example.social_media_app.service.impl;

import com.example.social_media_app.model.Post;
import com.example.social_media_app.model.User;
import com.example.social_media_app.repository.PostRepository;
import com.example.social_media_app.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Post createPost(String content, User author) {
        Post p = Post.builder()
                .content(content)
                .author(author)
                .build();
        return postRepository.save(p);
    }
}
