package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.exceptions.FriendRequestException;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.FriendRequest;
import com.example.socialmediaapi.model.enums.FriendRequestStatus;
import com.example.socialmediaapi.model.enums.FriendShipStatus;
import com.example.socialmediaapi.repository.FriendRequestRepository;
import com.example.socialmediaapi.service.FriendRequestService;
import com.example.socialmediaapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FriendRequestServiceImpl implements FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void sendFriendRequest(String emailSender, String emailReceiver) {

        log.info("find our sender and receiver users by email ");
        if (emailReceiver.equals(emailSender)) {
            throw new FriendRequestException("Sender's email and receiver's email are the same. A user cannot send a friend request to themselves.");
        }
        if (emailSender == null || emailReceiver == null) {
            throw new NullPointerException("Either sender's email or receiver's email is null.");
        }
        if (friendRequestRepository.existsBySenderUserEmailAndReceiverUserEmail(emailSender, emailReceiver)) {
            throw new FriendRequestException("A friend request from this sender to this receiver already exists.");
        }

        UserDto sender = userService.findByEmail(emailSender);
        UserDto receiver = userService.findByEmail(emailReceiver);


        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setReceiverUser(userMapper.toEntity(receiver));
        friendRequest.setSenderUser(userMapper.toEntity(sender));
        friendRequest.setStatus(FriendRequestStatus.PENDING);
        friendRequest.setFriendShipStatusSenderUser(FriendShipStatus.FOLLOWER);
        friendRequest.setFriendShipStatusReceiverUser(FriendShipStatus.BLOGGER);

        //friendRequest.getSenderUser().setSentFriendRequests(List.of(friendRequest));

        log.info("try to save friend request ");
        friendRequestRepository.save(friendRequest);
    }

    @Override
    @Transactional
    public void acceptFriendRequest(Long id) {
        if (id == null) {
            throw new NullPointerException("this id value for friendRequest us null ");
        }
        log.info("find our request by id ");
        FriendRequest friendRequestById = friendRequestRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(" FriendRequest with id " + id + " not found"));

        log.info("set to request new parameters ");
        friendRequestById.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequestById.setFriendShipStatusReceiverUser(FriendShipStatus.FRIENDS);
        friendRequestById.setFriendShipStatusSenderUser(FriendShipStatus.FRIENDS);

    }

    @Override
    @Transactional
    public void rejectFriendRequest(Long id) {
        if (id == null) {
            throw new NullPointerException("this id value for friendRequest us null ");
        }
        log.info("find our request by id");
        FriendRequest friendRequest = friendRequestRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FriendRequest with id " + id + " not found"));

        log.info("set to request new parameters ");
        if (friendRequest.getStatus() == FriendRequestStatus.PENDING) {
            friendRequest.setStatus(FriendRequestStatus.REJECTED);
        } else {
            throw new FriendRequestException("Status for reject should be PENDING.");
        }

    }

    @Override
    @Transactional
    public void changeFriendStatus(String userEmail, Long requestId) {
        if (userEmail == null) {
            throw new NullPointerException("userEmail is Null");
        }

        if (requestId == null) {
            throw new NullPointerException("requestId is Null");
        }

        FriendRequest friendRequestById = friendRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("FriendRequest with id " + requestId + " is not found"));

        modifyFriendRequestStatuses(friendRequestById, userEmail);

    }


    private void modifyFriendRequestStatuses(FriendRequest friendRequest, String userEmail) {
        if (userEmail.equals(friendRequest.getReceiverUser().getEmail())
                && (friendRequest.getFriendShipStatusReceiverUser() == FriendShipStatus.FRIENDS)) {

            friendRequest.setFriendShipStatusReceiverUser(FriendShipStatus.BLOGGER);
            friendRequest.setFriendShipStatusSenderUser(FriendShipStatus.FOLLOWER);

        } else if (userEmail.equals(friendRequest.getSenderUser().getEmail())
                && (friendRequest.getFriendShipStatusSenderUser() == FriendShipStatus.FRIENDS)) {

            friendRequest.setFriendShipStatusReceiverUser(FriendShipStatus.FOLLOWER);
            friendRequest.setFriendShipStatusSenderUser(FriendShipStatus.BLOGGER);
        } else {
            throw new FriendRequestException("Status is not Friends between users");
        }
    }

}
