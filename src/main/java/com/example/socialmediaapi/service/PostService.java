package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.PostDto;
import com.example.socialmediaapi.dto.post.PostInputDto;
import com.example.socialmediaapi.model.Post;

import java.security.Principal;

public interface PostService {
    PostDto createPost (PostInputDto postInputDto,Principal emailTarget);
    PostDto updatePost (PostInputDto postInputDto,Principal principal);
    PostDto getPostByTitle(String title);
    void delete(Long id, Principal principal);
    Post findById(Long id);


}
