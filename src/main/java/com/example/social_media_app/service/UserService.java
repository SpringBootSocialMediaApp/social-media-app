package com.example.social_media_app.service;

import com.example.social_media_app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void save(User user);

    void registerUser(User user);
    User findByEmail(String email);
    boolean existsByEmail(String email);

    // NEW
    Page<User> findAllUsersExceptCurrent(Long currentUserId, Pageable pageable);
}
