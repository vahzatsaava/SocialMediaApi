package com.example.socialmediaapi.service;

import com.example.socialmediaapi.dto.ActivityFeedDto;
import com.example.socialmediaapi.dto.PostDto;
import com.example.socialmediaapi.dto.content.UserActivityFeedDto;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ActivityFeedService {
    List<ActivityFeedDto> getActivityFeedsByUserEmail(String email, Sort.Direction sortOrder);

    ActivityFeedDto createActivityFeed(UserActivityFeedDto userActivityFeedDto, PostDto postDto);

    ActivityFeedDto changeStatusToViewed(Long activityId);

    void deleteActivityFeedByPostId(Long postId);

    void unsubscribeFromUser(Long userPreviousTargetId);

}
