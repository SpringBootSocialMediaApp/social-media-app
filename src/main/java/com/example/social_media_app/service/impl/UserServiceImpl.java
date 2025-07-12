package com.example.social_media_app.service.impl;

import com.example.social_media_app.model.User;
import com.example.social_media_app.repository.UserRepository;
import com.example.social_media_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // NEW
    @Override
    public Page<User> findAllUsersExceptCurrent(Long currentUserId, Pageable pageable) {
        return userRepository.findAllExceptCurrentUser(currentUserId, pageable);
    }
    
    // NEW - for friend suggestions
    @Override
    public Page<User> findUsersForFriendSuggestions(Long currentUserId, Pageable pageable) {
        return userRepository.findUsersForFriendSuggestions(currentUserId, pageable);
    }
}
