package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.dto.ActivityFeedDto;
import com.example.socialmediaapi.dto.general.ResponseDto;
import com.example.socialmediaapi.service.ActivityFeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/activity-feed")
@SecurityRequirement(name = "bearerAuth")
public class ActivityFeedController {
    private final ActivityFeedService activityFeedService;


    @GetMapping("/activity")
    @Operation(summary = "[US 5.1] Follower Can Watch Target's Posts",
            description = "This API is used to add ability to watch and sort all target's posts. By ASС and DESC")
    public ResponseEntity<ResponseDto<List<ActivityFeedDto>>> get(@RequestParam String email,
                                                                  @RequestParam(required = false, defaultValue = "DESC") String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        List<ActivityFeedDto> activityFeedDto = activityFeedService.getActivityFeedsByUserEmail(email, direction);
        ResponseDto<List<ActivityFeedDto>> responseDto = new ResponseDto<>(HttpStatus.OK.value(), activityFeedDto);
        return ResponseEntity.ok(responseDto);
    }

}
