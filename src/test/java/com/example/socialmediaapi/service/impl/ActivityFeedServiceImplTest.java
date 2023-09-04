package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.ActivityFeedDto;
import com.example.socialmediaapi.dto.PostDto;
import com.example.socialmediaapi.dto.content.UserActivityFeedDto;
import com.example.socialmediaapi.mapper.ActivityFeedMapper;
import com.example.socialmediaapi.model.ActivityFeed;
import com.example.socialmediaapi.repository.ActivityFeedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityFeedServiceImplTest {

    @InjectMocks
    private ActivityFeedServiceImpl activityFeedService;

    @Mock
    private ActivityFeedRepository activityFeedRepository;

    @Mock
    private ActivityFeedMapper activityFeedMapper;
    /*
    @Test
    void getActivityFeedsByUserEmail() {
        String userEmail = "user@example.com";
        Sort.Direction sortOrder = Sort.Direction.ASC;
        List<ActivityFeed> activityFeedList = new ArrayList<>();
        when(activityFeedRepository.findByUserEmailOrderByPostTimestampAsc(userEmail)).thenReturn(activityFeedList);
        lenient().when(activityFeedMapper.toDto(any(ActivityFeed.class))).thenReturn(new ActivityFeedDto());

        List<ActivityFeedDto> result = activityFeedService.getActivityFeedsByUserEmail(userEmail, sortOrder);

        assertNotNull(result);
        verify(activityFeedRepository).findByUserEmailOrderByPostTimestampAsc(userEmail);
        verify(activityFeedMapper, times(activityFeedList.size())).toDto(any(ActivityFeed.class));
    }

     */

    @Test
    void createActivityFeed() {
        UserActivityFeedDto userActivityFeedDto = new UserActivityFeedDto();
        PostDto postDto = new PostDto();
        ActivityFeedDto expectedActivityFeedDto = new ActivityFeedDto();
        when(activityFeedMapper.toEntity(any(ActivityFeedDto.class))).thenReturn(new ActivityFeed());
        when(activityFeedRepository.save(any(ActivityFeed.class))).thenReturn(new ActivityFeed());

        ActivityFeedDto result = activityFeedService.createActivityFeed(userActivityFeedDto, postDto);

        assertNotNull(result);
        verify(activityFeedRepository).save(any(ActivityFeed.class));
    }

    @Test
    void deleteActivityFeedByPostId() {
        Long postId = 1L;
        List<ActivityFeed> activityFeeds = new ArrayList<>();
        when(activityFeedRepository.findByPostId(postId)).thenReturn(activityFeeds);

        activityFeedService.deleteActivityFeedByPostId(postId);

        verify(activityFeedRepository).deleteAll(activityFeeds);
    }

    @Test
    void unsubscribeFromUser() {
        Long userPreviousTargetId = 1L;

        activityFeedService.unsubscribeFromUser(userPreviousTargetId);

        verify(activityFeedRepository).deleteAllByUserId(userPreviousTargetId);
    }
}