package com.example.social_media_app.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateDto {

    @NotBlank(message = "Comment content cannot be empty")
    @Size(max = 500, message = "Comment content cannot exceed 500 characters")
    private String content;
}
