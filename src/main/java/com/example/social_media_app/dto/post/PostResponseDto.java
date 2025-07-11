package com.example.social_media_app.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {
    private Long id;
    private String content;
    private UserInfoDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer likeCount;
    private Integer commentCount;
    private boolean likedByCurrentUser;
    private List<CommentDto> comments;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserInfoDto {
        private Long id;
        private String firstName;
        private String lastName;
        private String username;
        private String profilePicture;
    }
}
