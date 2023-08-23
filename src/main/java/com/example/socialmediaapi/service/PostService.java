package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.PostDto;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface PostService {
    PostDto createPost (String title, String text,MultipartFile image, String emailTarget);
    PostDto updatePost (String title, String text, MultipartFile image, Long postId,Principal principal);
    PostDto getPostByTitle(String title);
    void delete(Long id, Principal principal);

}
