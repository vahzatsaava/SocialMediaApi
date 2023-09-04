package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.ActivityFeedDto;
import com.example.socialmediaapi.dto.PostDto;
import com.example.socialmediaapi.dto.content.UserActivityFeedDto;
import com.example.socialmediaapi.mapper.ActivityFeedMapper;
import com.example.socialmediaapi.model.ActivityFeed;
import com.example.socialmediaapi.model.enums.ActivityReadStatus;
import com.example.socialmediaapi.repository.ActivityFeedRepository;
import com.example.socialmediaapi.service.ActivityFeedService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ActivityFeedServiceImpl implements ActivityFeedService {
    private final ActivityFeedRepository activityFeedRepository;
    private final ActivityFeedMapper activityFeedMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ActivityFeedDto> getActivityFeedsByUserEmail(Principal principal, Sort.Direction sortOrder) {

        if (sortOrder == null) {
            throw new IllegalArgumentException ("sortOrder is null getActivityFeed method");

        }
        List<ActivityFeed> activityFeeds;
        if (sortOrder == Sort.Direction.ASC) {
            activityFeeds = activityFeedRepository.findByUserEmailOrderByPostTimestampAsc(principal.getName());
        } else {
            activityFeeds = activityFeedRepository.findByUserEmailOrderByPostTimestampDesc(principal.getName());
        }
        return activityFeeds.stream()
                .map(activityFeedMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ActivityFeedDto createActivityFeed(UserActivityFeedDto userActivityFeedDto,PostDto postDto) {
        if (userActivityFeedDto == null) {
            throw new IllegalArgumentException ("userActivityFeedDto is null in createActivityFeed method");
        }
        if (postDto == null) {
            throw new IllegalArgumentException ("postDto is null in createActivityFeed method");
        }

        ActivityFeedDto activityFeedDto = new ActivityFeedDto();
        activityFeedDto.setPost(postDto);
        activityFeedDto.setStatusChanged(LocalDateTime.now());
        activityFeedDto.setUser(userActivityFeedDto);
        activityFeedDto.setActivityReadStatus(ActivityReadStatus.NOT_VIEWED);

        ActivityFeed forSave = activityFeedMapper.toEntity(activityFeedDto);
        activityFeedRepository.save(forSave);

        return activityFeedDto;
    }

    @Override
    @Transactional
    public ActivityFeedDto changeStatusToViewed(Long activityId) {
        if (activityId == null){
            throw new IllegalArgumentException ("activityId is null in changeStatusToViewed method");
        }
        ActivityFeed activityFeed = activityFeedRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("ActivityFeed not found by id " + activityId));
        activityFeed.setActivityReadStatus(ActivityReadStatus.VIEWED);
        return activityFeedMapper.toDto(activityFeed);
    }

    @Override
    @Transactional
    public void deleteActivityFeedByPostId(Long postId) {
        if (postId == null){
            throw  new IllegalArgumentException ("postId in findByPostId in class ActivityFeedService is null");
        }
        List<ActivityFeed> activityFeeds = activityFeedRepository
                .findByPostId(postId);

        activityFeedRepository.deleteAll(activityFeeds);
    }

    @Override
    @Transactional
    public void unsubscribeFromUser(Long userPreviousTargetId) {
        if (userPreviousTargetId == null){
            throw  new IllegalArgumentException ("userPreviousTargetId in userPreviousTargetId in class ActivityFeedService is null");
        }
        activityFeedRepository.deleteAllByUserId(userPreviousTargetId);
    }
}
