package com.example.socialmediaapi.mapper;

import com.example.socialmediaapi.dto.ActivityFeedDto;
import com.example.socialmediaapi.model.ActivityFeed;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {UserActivityFeedMapper.class})
public interface ActivityFeedMapper {
    ActivityFeedDto toDto(ActivityFeed activityFeed);

    @InheritInverseConfiguration
    ActivityFeed toEntity(ActivityFeedDto activityFeedDto);
}
