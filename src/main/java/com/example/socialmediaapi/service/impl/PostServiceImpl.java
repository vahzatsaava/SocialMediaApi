package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.PostDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.mapper.ImageMapper;
import com.example.socialmediaapi.mapper.PostMapper;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.Post;
import com.example.socialmediaapi.model.User;
import com.example.socialmediaapi.repository.PostRepository;
import com.example.socialmediaapi.service.PostService;
import com.example.socialmediaapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final UserService userService;
    private final PostRepository postRepository;

    private final PostMapper postMapper;
    private final ImageMapper imageMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public PostDto createPost(PostDto postDto, String email) {
        if (postDto == null){
            throw new NullPointerException("PostDto is null please check your input if you want to save Post");
        }
        log.info("we are finding our user by email and set them in post");
        UserDto user = userService.findByEmail(email);

        Post mapToPost = postMapper.toEntity(postDto);
        mapToPost.setUser(userMapper.toEntity(user));

        log.info("Trying to save post");
        Post post = postRepository.save(mapToPost);

        return postMapper.toDto(post);
    }

    @Override
    @Transactional
    public PostDto updatePost(PostDto postDto, Long postId) {
        log.info("try to find post by id ");
        Post postById = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " is not found"));
        log.info("Update our post ");
        postById.setText(postDto.getText());
        postById.setTitle(postDto.getTitle());
        postById.setImages(imageMapper.toEntity(postDto.getImages()));

        Post savedPost = postRepository.save(postById);
        return postMapper.toDto(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostByTitle(String title) {
        log.info("try to find post by title ");
        Post post = postRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Post with title " + title + " not found"));

        return postMapper.toDto(post);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + id + " not found"));
        log.info("delete our post by id");
        postRepository.delete(post);
    }

}
