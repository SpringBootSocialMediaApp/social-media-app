package com.example.social_media_app.service;

import com.example.social_media_app.model.entity.User;

public interface UserService {
    User registerUser(User user);
    User findByEmail(String email);
}
