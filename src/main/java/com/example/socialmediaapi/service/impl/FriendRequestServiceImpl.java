package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.FriendShipDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.exceptions.FriendRequestException;
import com.example.socialmediaapi.mapper.*;
import com.example.socialmediaapi.model.*;
import com.example.socialmediaapi.model.enums.FriendRequestStatus;
import com.example.socialmediaapi.repository.FriendRequestRepository;
import com.example.socialmediaapi.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
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
    public void sendFriendRequestAndSubscribe(Principal principal, String emailReceiver) {
        checkArgumentsNotNull(emailReceiver,principal);

        validateNoExistingFriendRequest(principal.getName(), emailReceiver);

        UserDto senderDto = userService.findByEmail(principal.getName());
        UserDto receiverDto = userService.findByEmail(emailReceiver);

        User sender = userMapper.toEntity(senderDto);
        User receiver = userMapper.toEntity(receiverDto);

        saveFriendRequest(sender,receiver);

        subscriptionService.save(sender, receiver);

        List<Post> posts = receiver.getPosts();
        for (Post post : posts) {
            activityFeedService.createActivityFeed(userActivityFeedMapper.toDto(sender), postMapper.toDto(post));
        }

    }


    @Override
    @Transactional
    public void acceptFriendRequest(String senderRequestUser, Principal principal) {
        checkArgumentsNotNull(senderRequestUser,principal);

        FriendRequest friendRequestById = getFriendRequest(senderRequestUser,principal.getName());

        User sender = friendRequestById.getSenderUser();
        User receiver = friendRequestById.getReceiverUser();

        if (friendRequestById.getStatus() == FriendRequestStatus.PENDING) {
            friendRequestById.setStatus(FriendRequestStatus.ACCEPTED);

            subscriptionService.save(receiver, sender);

            saveFriendship(sender, receiver);

            saveAcceptedFriendRequest(sender, receiver);

            List<Post> posts = sender.getPosts();
            for (Post post : posts) {
                activityFeedService.createActivityFeed(userActivityFeedMapper.toDto(receiver), postMapper.toDto(post));
            }
        } else {
            throw new FriendRequestException("Status should be Pending for Accept sender friend request");
        }
    }

    @Override
    @Transactional
    public void rejectFriendRequest(String senderRequestUser, Principal principal) {
        checkArgumentsNotNull(senderRequestUser,principal);

        FriendRequest friendRequest = getFriendRequest(senderRequestUser,principal.getName());

        if (friendRequest.getStatus() == FriendRequestStatus.PENDING) {
            friendRequest.setStatus(FriendRequestStatus.REJECTED);
        } else {
            throw new FriendRequestException("Status for reject should be PENDING ");
        }
    }

    @Override
    @Transactional
    public void deleteFromFriends(Principal principal, String userToDeleteEmail) {
        checkArgumentsNotNull(userToDeleteEmail,principal);

        FriendRequest friendRequest = getFriendRequest(principal.getName(),userToDeleteEmail);

        User sender = friendRequest.getSenderUser();

        if (existFriendRequestByStatus(principal.getName(), userToDeleteEmail) || existFriendRequestByStatus(userToDeleteEmail, principal.getName())) {
            friendRequestRepository.deleteBySenderUserEmailOrReceiverUserEmail(principal.getName(), userToDeleteEmail);
            subscriptionService.deleteByFollowerAndTargetEmails(principal.getName(), userToDeleteEmail);
            friendShipService.deleteFromFriendsByEmails(principal.getName(), userToDeleteEmail);
            activityFeedService.unsubscribeFromUser(sender.getId());
        } else {
            throw new FriendRequestException("Status should be ACCEPTED for changing ");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existFriendRequestByStatus(String senderEmail, String receiverEmail) {

        return friendRequestRepository.existsFriendRequestBySenderUserEmailAndReceiverUserEmail(senderEmail, receiverEmail);
    }

    private FriendRequest getFriendRequest(String senderUser, String receiverUser){
        return friendRequestRepository.findBySenderUserEmailAndReceiverUserEmail(senderUser, receiverUser)
                .orElseThrow(() -> new FriendRequestException("no friend requests between users")) ;
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
    private void checkArgumentsNotNull(String email,Principal principal){
        if (email == null) {
            throw new IllegalArgumentException("Either sender's email or receiver's email is null.");
        }
        if (email.equals(principal.getName())) {
            throw new IllegalArgumentException("Sender's email and receiver's email are the same. A user cannot send a friend request to themselves.");
        }
    }

}
