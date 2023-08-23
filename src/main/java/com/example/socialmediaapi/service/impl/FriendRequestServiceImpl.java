package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.FriendShipDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.exceptions.FriendRequestException;
import com.example.socialmediaapi.mapper.*;
import com.example.socialmediaapi.model.*;
import com.example.socialmediaapi.model.enums.FriendRequestStatus;
import com.example.socialmediaapi.repository.FriendRequestRepository;
import com.example.socialmediaapi.service.*;
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
    private final SubscriptionService subscriptionService;
    private final FriendShipService friendShipService;
    private final ActivityFeedService activityFeedService;


    private final UserMapper userMapper;
    private final FriendShipMapper friendShipMapper;
    private final UserActivityFeedMapper userActivityFeedMapper;
    private final PostMapper postMapper;

    @Override
    @Transactional
    public void sendFriendRequestAndSubscribe(String emailSender, String emailReceiver) {

        if (emailSender == null || emailReceiver == null) {
            throw new IllegalArgumentException("Either sender's email or receiver's email is null.");
        }
        log.info("find our sender and receiver users by email ");
        if (emailReceiver.equals(emailSender)) {
            throw new IllegalArgumentException("Sender's email and receiver's email are the same. A user cannot send a friend request to themselves.");
        }

        validateNoExistingFriendRequest(emailSender, emailReceiver);

        log.info("get users by email and map it to dto ");
        UserDto senderDto = userService.findByEmail(emailSender);
        UserDto receiverDto = userService.findByEmail(emailReceiver);

        User sender = userMapper.toEntity(senderDto);
        User receiver = userMapper.toEntity(receiverDto);


        log.info("try to save friend request ");
        saveFriendRequest(sender,receiver);

        log.info("create subscription from sender to user");
        subscriptionService.save(sender, receiver);

        List<Post> posts = receiver.getPosts();
        for (Post post : posts) {
            log.info("add ability follower user watch posts");
            activityFeedService.createActivityFeed(userActivityFeedMapper.toDto(sender), postMapper.toDto(post));
        }

    }


    @Override
    @Transactional
    public void acceptFriendRequest(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("this id value for friendRequest us null ");
        }
        log.info("find our request by id ");
        FriendRequest friendRequestById = findFriendRequestById(id);

        User sender = friendRequestById.getSenderUser();
        User receiver = friendRequestById.getReceiverUser();

        log.info("set to request new parameters ");
        if (friendRequestById.getStatus() == FriendRequestStatus.PENDING) {
            friendRequestById.setStatus(FriendRequestStatus.ACCEPTED);

            log.info("subscribe operation to from receiver to sender user");
            subscriptionService.save(receiver, sender);

            log.info("Create friendship between users ");
            saveFriendship(sender, receiver);

            saveAcceptedFriendRequest(sender, receiver);

            List<Post> posts = sender.getPosts();
            for (Post post : posts) {
                log.info("add ability to target user watch posts follower users");
                activityFeedService.createActivityFeed(userActivityFeedMapper.toDto(receiver), postMapper.toDto(post));
            }
        } else {
            throw new FriendRequestException("Status should be Pending for Accept sender friend request");
        }
    }

    @Override
    @Transactional
    public void rejectFriendRequest(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("this id value for friendRequest us null ");
        }
        log.info("find our request by id");
        FriendRequest friendRequest = friendRequestRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FriendRequest with id " + id + " not found"));

        log.info("set to request new parameters ");
        if (friendRequest.getStatus() == FriendRequestStatus.PENDING) {
            friendRequest.setStatus(FriendRequestStatus.REJECTED);
        } else {
            throw new FriendRequestException("Status for reject should be PENDING ");
        }
    }

    @Override
    @Transactional
    public void deleteFromFriends(String senderUserEmail, String userToDeleteEmail) {
        if (senderUserEmail == null) {
            throw new IllegalArgumentException("userEmail is Null");
        }

        if (userToDeleteEmail == null) {
            throw new IllegalArgumentException("requestId is Null");
        }
        FriendRequest friendRequest =
                friendRequestRepository.findBySenderUserEmailAndReceiverUserEmail(senderUserEmail, userToDeleteEmail);
        User sender = friendRequest.getSenderUser();

        if (existFriendRequestByStatus(senderUserEmail, userToDeleteEmail) || existFriendRequestByStatus(userToDeleteEmail, senderUserEmail)) {
            friendRequestRepository.deleteBySenderUserEmailOrReceiverUserEmail(senderUserEmail, userToDeleteEmail);
            subscriptionService.deleteByFollowerAndTargetEmails(senderUserEmail, userToDeleteEmail);
            friendShipService.deleteFromFriendsByEmails(senderUserEmail, userToDeleteEmail);
            activityFeedService.unsubscribeFromUser(sender.getId());
        } else {
            throw new FriendRequestException("Status should be ACCEPTED for changing ");
        }
    }

    @Override
    public Boolean existFriendRequestByStatus(String senderEmail, String receiverEmail) {

        return friendRequestRepository.existsFriendRequestBySenderUserEmailAndReceiverUserEmail(senderEmail, receiverEmail);
    }

    private FriendRequest findFriendRequestById(Long id) {
        return friendRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FriendRequest with ID " + id + " not found"));
    }

    private void saveAcceptedFriendRequest(User sender, User receiver) {
        FriendRequest newFriendRequest = new FriendRequest();
        newFriendRequest.setSenderUser(receiver);
        newFriendRequest.setReceiverUser(sender);
        newFriendRequest.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(newFriendRequest);
    }
    private void saveFriendRequest(User sender, User receiver) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setReceiverUser(receiver);
        friendRequest.setSenderUser(sender);
        friendRequest.setStatus(FriendRequestStatus.PENDING);
        friendRequestRepository.save(friendRequest);
    }

    private FriendShipDto saveFriendship(User sender, User receiver) {

        FriendShip friendShip = new FriendShip();
        friendShip.setUser1(sender);
        friendShip.setUser2(receiver);
        FriendShipDto friendShipDto = friendShipMapper.toFriendShipDto(friendShip);

        return friendShipService.save(friendShipDto);
    }
    private void validateNoExistingFriendRequest(String emailSender, String emailReceiver) {
        if (Boolean.TRUE.equals(existFriendRequestByStatus(emailSender, emailReceiver))) {
            throw new FriendRequestException("A friend request from this sender to this receiver already exists.");
        }
    }

}
