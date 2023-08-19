package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.PostDto;

public interface PostService {
    PostDto createPost (PostDto postDto,String email);
    PostDto updatePost (PostDto postDto,Long postId);
    PostDto getPostByTitle(String title);
    void delete(Long id);
}
