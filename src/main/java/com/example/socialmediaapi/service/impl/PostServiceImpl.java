package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.ImageDto;
import com.example.socialmediaapi.dto.PostDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.dto.content.UserActivityFeedDto;
import com.example.socialmediaapi.exceptions.InternalServerErrorException;
import com.example.socialmediaapi.exceptions.PostCreatException;
import com.example.socialmediaapi.exceptions.UnauthorizedException;
import com.example.socialmediaapi.mapper.ImageMapper;
import com.example.socialmediaapi.mapper.PostMapper;
import com.example.socialmediaapi.mapper.UserActivityFeedMapper;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.Image;
import com.example.socialmediaapi.model.Post;
import com.example.socialmediaapi.repository.ImageRepository;
import com.example.socialmediaapi.repository.PostRepository;
import com.example.socialmediaapi.service.*;
import com.example.socialmediaapi.utils.PhotoValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final UserService userService;
    private final PostRepository postRepository;

    private final ActivityFeedService activityFeedService;
    private final SubscriptionService subscriptionService;

    private final ImageService imageService;


    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final UserActivityFeedMapper userActivityFeedMapper;

    @Override
    @Transactional
    public PostDto createPost(String title, String text, MultipartFile image, String emailTargetUser) {
        if (title == null || emailTargetUser == null || text == null || image == null) {
            throw new IllegalArgumentException("One or more required fields are null");
        }

        UserDto userTarget = userService.findByEmail(emailTargetUser);
        Image imageEntity = imageService.saveImage(image);

        Post post = createPostEntity(userTarget, title, text, imageEntity);
        Post postSaved = postRepository.save(post);

        imageEntity.setPost(postSaved);

        createActivityFeeds(emailTargetUser, postSaved);

        return postMapper.toDto(postSaved);
    }


    @Override
    @Transactional
    public PostDto updatePost(String title, String text, MultipartFile image, Long postId, Principal principal) {
        if (title == null || text == null || image == null) {
            throw new IllegalArgumentException("One or more required fields are null");
        }
        Image imageEntity = imageService.saveImage(image);

        log.info("try to find post by id ");
        Post postById = findById(postId);

        log.info("!!" + principal.getName());

        if (!postById.getUser().getEmail().equals(principal.getName())) {
            throw new UnauthorizedException("You are not authorized to delete this post");
        }

        log.info("Update our post ");

        updatePostFields(postById, title, text, imageEntity);

        imageEntity.setPost(postById);

        return postMapper.toDto(postById);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostByTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("title is null getPostByTitle is canceled");
        }
        log.info("try to find post by title ");
        Post post = postRepository.findByTitle(title)
                .orElseThrow(() -> new PostCreatException("Post with title " + title + " not found"));

        return postMapper.toDto(post);
    }

    @Override
    @Transactional
    public void delete(Long id, Principal principal) {
        if (id == null) {
            throw new IllegalArgumentException("id is null delete operation is cancelled");
        }
        Post post = findById(id);

        if (!post.getUser().getEmail().equals(principal.getName())) {
            throw new UnauthorizedException("You are not authorized to delete this post");
        }

        log.info("delete our post by id");
        postRepository.delete(post);

        log.info("delete all activities with this post");
        activityFeedService.deleteActivityFeedByPostId(id);
    }

    private Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostCreatException("Post with id " + id + " not found"));
    }

    private Post createPostEntity(UserDto userTarget, String title, String text, Image imageEntity) {
        Post post = new Post();
        post.setUser(userMapper.toEntity(userTarget));
        post.setTitle(title);
        post.setText(text);
        post.setTimestamp(LocalDateTime.now());
        post.setImages(List.of(imageEntity));

        return post;
    }


    private void createActivityFeeds(String emailTargetUser, Post postSaved) {
        List<UserDto> userDtos = subscriptionService.findByTargetUserEmail(emailTargetUser);
        userDtos.forEach(users -> {
            UserActivityFeedDto userActivityFeedDto = userActivityFeedMapper.toDto(users);
            activityFeedService.createActivityFeed(userActivityFeedDto, postMapper.toDto(postSaved));
        });
    }

    private void updatePostFields(Post postById, String title, String text, Image imageEntity) {
        postById.setText(text);
        postById.setTitle(title);
        postById.setTimestamp(LocalDateTime.now());

        if (postById.getImages() == null) {
            postById.setImages(List.of(imageEntity));
        } else {
            postById.getImages().add(imageEntity);
        }
    }
}
