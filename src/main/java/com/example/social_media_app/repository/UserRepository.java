package com.example.social_media_app.repository;

import com.example.social_media_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.social_media_app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id <> :currentUserId")
    Page<User> findAllExceptCurrentUser(@Param("currentUserId") Long currentUserId, Pageable pageable);
}