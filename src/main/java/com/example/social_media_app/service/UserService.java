package com.example.social_media_app.service;

import com.example.social_media_app.model.User;

public interface UserService {

    void registerUser(User user);
    User findByEmail(String email);
    boolean existsByEmail(String email);


    // 🔽 ADD THIS
    User updateUser(User updatedUser);

}
