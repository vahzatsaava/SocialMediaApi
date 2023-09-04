package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.dto.general.ResponseDto;
import com.example.socialmediaapi.service.FriendRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/friends")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class FriendshipAndSubscriptionController {

    private final FriendRequestService friendRequestService;

    @PostMapping("/send-request")
    @Operation(summary = "[US 3.1] Send a friend request to a user",
            description = "This API is used to send friend requests.")
    public ResponseEntity<ResponseDto<String>> sendFriendRequest(Principal principal,
                                                                 @RequestParam @Size(min = 8) @Email String emailReceiver) {
        friendRequestService.sendFriendRequestAndSubscribe(principal, emailReceiver);
        ResponseDto<String> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                String.format("User with email %s successfully sent a friend request to the user with email %s", principal.getName(), emailReceiver));
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/accept-request/{id}")
    @Operation(summary = "[US 3.2] Accept a friend request from another user",
            description = "This API is used to accept user friend requests.")
    public ResponseEntity<ResponseDto<String>> acceptFriendRequest(@PathVariable Long id) {
        friendRequestService.acceptFriendRequest(id);
        ResponseDto<String> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                String.format("User with id - %s was successfully accepted the friend request", id));
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/reject-request/{id}")
    @Operation(summary = "[US 3.3] Reject a friend request from another user",
            description = "This API is used to reject user friend requests.")
    public ResponseEntity<ResponseDto<String>> rejectFriendRequest(@PathVariable Long id) {
        friendRequestService.rejectFriendRequest(id);
        ResponseDto<String> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                String.format("User with ID %s has rejected the friend request", id));
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/change-friend-status/{userToDelete}")
    @Operation(summary = "[US 3.4] Unfollow and Remove Friend",
            description = "This API allows a user to unfollow and remove another user from their friends list. " +
                    "As a result, the user will no longer receive posts from the removed friend in their feed.")
    public ResponseEntity<ResponseDto<String>> deleteFriend(Principal principal,
                                                            @PathVariable @Size(min = 8) @Email String userToDelete) {
        friendRequestService.deleteFromFriends(principal, userToDelete);
        ResponseDto<String> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                String.format("Friend status was changed by the user with email %s", principal));
        return ResponseEntity.ok(responseDto);
    }
}
