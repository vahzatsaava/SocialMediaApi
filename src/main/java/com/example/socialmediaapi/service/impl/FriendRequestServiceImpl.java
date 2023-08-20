package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.FriendShipDto;
import com.example.socialmediaapi.dto.UserDto;
import com.example.socialmediaapi.exceptions.FriendRequestException;
import com.example.socialmediaapi.mapper.FriendShipMapper;
import com.example.socialmediaapi.mapper.SubscriptionMapper;
import com.example.socialmediaapi.mapper.UserMapper;
import com.example.socialmediaapi.model.FriendRequest;
import com.example.socialmediaapi.model.FriendShip;
import com.example.socialmediaapi.model.Subscription;
import com.example.socialmediaapi.model.User;
import com.example.socialmediaapi.model.enums.FriendRequestStatus;
import com.example.socialmediaapi.repository.FriendRequestRepository;
import com.example.socialmediaapi.service.FriendRequestService;
import com.example.socialmediaapi.service.FriendShipService;
import com.example.socialmediaapi.service.SubscriptionService;
import com.example.socialmediaapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Slf4j
public class FriendRequestServiceImpl implements FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final FriendShipService friendShipService;

    private final UserMapper userMapper;
    private final FriendShipMapper friendShipMapper;
    private final SubscriptionMapper subscriptionMapper;

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
        boolean value = existFriendRequestByStatus(emailSender,emailReceiver);
        if (value) {
            throw new FriendRequestException("A friend request from this sender to this receiver already exists.");
        }

        log.info("get users by email and map it to dto ");
        UserDto senderDto = userService.findByEmail(emailSender);
        UserDto receiverDto = userService.findByEmail(emailReceiver);
        log.info("asdfsdfsdfsdf");


        User sender = userMapper.toEntity(senderDto);
        User receiver = userMapper.toEntity(receiverDto);


        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setReceiverUser(receiver);
        friendRequest.setSenderUser(sender);
        friendRequest.setStatus(FriendRequestStatus.PENDING);

        log.info("try to save friend request ");
        friendRequestRepository.save(friendRequest);

        log.info("create subscription from sender to user");
        subscriptionService.save(sender, receiver);

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
        if (friendRequestById.getStatus() == FriendRequestStatus.PENDING) {
            friendRequestById.setStatus(FriendRequestStatus.ACCEPTED);
            log.info("subscribe operation to from receiver to sender user");

            Subscription backSubscription = new Subscription();
            backSubscription.setTargetUser(friendRequestById.getReceiverUser());
            backSubscription.setFollowerUser(friendRequestById.getSenderUser());
            subscriptionService.save(backSubscription.getTargetUser(),backSubscription.getFollowerUser());

            log.info("Create friendship between users ");
            saveFriendship(friendRequestById.getSenderUser(), friendRequestById.getReceiverUser());

        } else {
            throw new FriendRequestException("Status should be Pending for Accept sender friend request");
        }
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
            throw new FriendRequestException("Status for reject should be PENDING ");
        }
    }

    @Override
    @Transactional
    public void changeFriendStatus(Long subscriptionId, Long requestId,Long friendShipID) {
        if (subscriptionId == null) {
            throw new NullPointerException("userEmail is Null");
        }

        if (requestId == null) {
            throw new NullPointerException("requestId is Null");
        }
        if (friendShipID == null) {
            throw new NullPointerException("friendShip is Null");
        }

        FriendRequest friendRequestById = friendRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("FriendRequest with id " + requestId + " is not found"));


        if (friendRequestById.getStatus() == FriendRequestStatus.ACCEPTED) {
            friendRequestById.setStatus(FriendRequestStatus.REJECTED);
            subscriptionService.delete(subscriptionId);
            friendShipService.delete(friendShipID);
        } else {
            throw new FriendRequestException("Status should be ACCEPTED for changing ");
        }
    }

    @Override
    public Boolean existFriendRequestByStatus(String senderEmail,String receiverEmail) {

        return friendRequestRepository.existsFriendRequestBySenderUserEmailAndReceiverUserEmail(senderEmail,receiverEmail);
    }


    private FriendShipDto saveFriendship(User sender, User receiver) {

        FriendShip friendShip = new FriendShip();
        friendShip.setUser1(sender);
        friendShip.setUser2(receiver);
        FriendShipDto friendShipDto = friendShipMapper.toFriendShipDto(friendShip);

        return friendShipService.save(friendShipDto);
    }
}
