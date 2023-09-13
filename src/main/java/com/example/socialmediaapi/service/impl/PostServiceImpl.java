package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.PostDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.dto.content.UserActivityFeedDto;
import com.example.socialmediaapi.dto.post.PostInputDto;
import com.example.socialmediaapi.exceptions.PostCreatException;
import com.example.socialmediaapi.exceptions.UnauthorizedException;
import com.example.socialmediaapi.mapper.PostMapper;
import com.example.socialmediaapi.mapper.UserActivityFeedMapper;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.Post;
import com.example.socialmediaapi.repository.PostRepository;
import com.example.socialmediaapi.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final UserActivityFeedMapper userActivityFeedMapper;

    @Override
    @Transactional
    public PostDto createPost(PostInputDto postInputDto, Principal emailTargetUser) {
        if (postInputDto == null) {
            throw new IllegalArgumentException("postInputDto  are null");
        }

        UserDto userTarget = userService.findByEmail(emailTargetUser.getName());

        Post post = createPostEntity(userTarget, postInputDto.getTitle(), postInputDto.getText());
        Post postSaved = postRepository.save(post);

        createActivityFeeds(emailTargetUser.getName(), postSaved);

        return postMapper.toDto(postSaved);
    }


    @Override
    @Transactional
    public PostDto updatePost(PostInputDto postInputDto, Principal principal) {
        if (postInputDto == null ) {
            throw new IllegalArgumentException("postInputDto is null");
        }

        Post postById = findById(postInputDto.getId());

        if (!postById.getUser().getEmail().equals(principal.getName())) {
            throw new UnauthorizedException("You are not authorized to delete this post");
        }

        updatePostFields(postById, postInputDto.getTitle(), postInputDto.getText());


        return postMapper.toDto(postById);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostByTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("title is null getPostByTitle is canceled");
        }
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

        postRepository.delete(post);

        activityFeedService.deleteActivityFeedByPostId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostCreatException("Post with id " + id + " not found"));
    }

    private Post createPostEntity(UserDto userTarget, String title, String text) {
        Post post = new Post();
        post.setUser(userMapper.toEntity(userTarget));
        post.setTitle(title);
        post.setText(text);
        post.setTimestamp(LocalDateTime.now());

        return post;
    }


    private void createActivityFeeds(String emailTargetUser, Post postSaved) {
        List<UserDto> userDtos = subscriptionService.findByTargetUserEmail(emailTargetUser);
        userDtos.forEach(users -> {
            UserActivityFeedDto userActivityFeedDto = userActivityFeedMapper.toDto(users);
            activityFeedService.createActivityFeed(userActivityFeedDto, postMapper.toDto(postSaved));
        });
    }

    private void updatePostFields(Post postById, String title, String text) {
        postById.setText(text);
        postById.setTitle(title);
        postById.setTimestamp(LocalDateTime.now());

    }
}
