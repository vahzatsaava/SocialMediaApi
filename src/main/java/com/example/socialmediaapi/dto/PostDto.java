package com.example.socialmediaapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long id;

    @NotBlank(message = "Title must not be blank")
    @Size(min = 3, message = "Title must be at least 3 characters long")
    private String title;

    private String text;

    private LocalDateTime timestamp;

    private List<ImageDto> images;
}
