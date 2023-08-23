package com.example.socialmediaapi.dto;

import com.example.socialmediaapi.dto.content.UserActivityFeedDto;
import com.example.socialmediaapi.model.enums.ActivityReadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityFeedDto {

    private UserActivityFeedDto user;
    private PostDto post;
    private ActivityReadStatus activityReadStatus;
    private LocalDateTime statusChanged;
}
