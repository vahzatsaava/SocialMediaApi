package com.example.socialmediaapi.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostInputDto {

    private Long id;

    @NotBlank(message = "Title must not be blank")
    @Size(min = 3, message = "Title must be at least 3 characters long")
    private String title;

    private String text;


}
