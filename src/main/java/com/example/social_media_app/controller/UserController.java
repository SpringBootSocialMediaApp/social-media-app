package com.example.social_media_app.controller;

import com.example.social_media_app.model.User;
import com.example.social_media_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ Get paginated list of users excluding the current user
    @GetMapping
    public Page<User> getAllUsersExceptCurrent(
            @RequestParam Long currentUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.findAllUsersExceptCurrent(currentUserId, pageable);
    }
}
