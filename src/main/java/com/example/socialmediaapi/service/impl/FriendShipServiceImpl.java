package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.FriendShipDto;
import com.example.socialmediaapi.mapper.FriendShipMapper;
import com.example.socialmediaapi.model.FriendShip;
import com.example.socialmediaapi.repository.FriendShipRepository;
import com.example.socialmediaapi.service.FriendShipService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;

@Service
@AllArgsConstructor
@Slf4j
public class FriendShipServiceImpl implements FriendShipService {
    private final FriendShipRepository friendShipRepository;
    private final FriendShipMapper friendShipMapper;

    @Override
    @Transactional
    public FriendShipDto save(FriendShipDto friendship) {
        if (friendship == null){
            throw new IllegalArgumentException ("this friendship values is null" );
        }
        log.info("try to save friendship");
        FriendShip savedFriendShip = friendShipRepository.save(friendShipMapper.toFriendShipEntity(friendship));

        return friendShipMapper.toFriendShipDto(savedFriendShip);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null){
            throw new IllegalArgumentException ("we cannot delete friendship because: id is null" );
        }
        FriendShip friendShip = friendShipRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity friendship with id " + id + " not found"));

        friendShipRepository.delete(friendShip);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isFriends(String emailUser1, String emailUser2) {
        if (emailUser1 == null){
            throw new IllegalArgumentException ("emailUser1 is null");
        }
        if (emailUser2 == null){
            throw new IllegalArgumentException ("emailUser2  is null");
        }
        boolean fromUser1ToUser2 = friendShipRepository.existsFriendShipByUser1EmailAndUser2Email(emailUser1,emailUser2);
        boolean fromUser2ToUser1 = friendShipRepository.existsFriendShipByUser1EmailAndUser2Email(emailUser2,emailUser1);
        return fromUser2ToUser1 || fromUser1ToUser2;
    }

    @Override
    @Transactional
    public void deleteFromFriendsByEmails(String senderUserEmail, String userToDeleteEmail) {
        if (senderUserEmail == null){
            throw new IllegalArgumentException ("senderUserEmail is null");
        }
        if (userToDeleteEmail == null){
            throw new IllegalArgumentException ("userToDeleteEmail  is null");
        }
        friendShipRepository.deleteByUser1EmailOrUser2Email(senderUserEmail,userToDeleteEmail);
        friendShipRepository.deleteByUser1EmailOrUser2Email(userToDeleteEmail,senderUserEmail);
    }
}
