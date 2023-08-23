package com.example.socialmediaapi.service.impl;

import com.example.socialmediaapi.dto.FriendShipDto;
import com.example.socialmediaapi.mapper.FriendShipMapper;
import com.example.socialmediaapi.model.FriendShip;
import com.example.socialmediaapi.repository.FriendShipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendShipServiceImplTest {
    @InjectMocks
    private FriendShipServiceImpl friendShipService;

    @Mock
    private FriendShipRepository friendShipRepository;

    @Mock
    private FriendShipMapper friendShipMapper;

    @Test
    void save() {
        FriendShipDto friendShipDto = new FriendShipDto();
        FriendShip friendShipEntity = new FriendShip();
        when(friendShipMapper.toFriendShipEntity(friendShipDto)).thenReturn(friendShipEntity);
        when(friendShipRepository.save(friendShipEntity)).thenReturn(friendShipEntity);
        when(friendShipMapper.toFriendShipDto(friendShipEntity)).thenReturn(friendShipDto);


        FriendShipDto result = friendShipService.save(friendShipDto);

        assertNotNull(result);
        verify(friendShipRepository).save(friendShipEntity);
        verify(friendShipMapper).toFriendShipDto(friendShipEntity);
    }

    @Test
    void delete() {
        Long id = 1L;
        FriendShip friendShip = new FriendShip();
        when(friendShipRepository.findById(id)).thenReturn(Optional.of(friendShip));

        friendShipService.delete(id);

        verify(friendShipRepository).delete(friendShip);
    }

    @Test
    void isFriends() {
        String emailUser1 = "user1@example.com";
        String emailUser2 = "user2@example.com";
        when(friendShipRepository.existsFriendShipByUser1EmailAndUser2Email(emailUser1, emailUser2)).thenReturn(true);

        Boolean result = friendShipService.isFriends(emailUser1, emailUser2);

        assertTrue(result);
    }

    @Test
    void deleteFromFriendsByEmails() {
        String senderUserEmail = "sender@example.com";
        String userToDeleteEmail = "user@example.com";

        friendShipService.deleteFromFriendsByEmails(senderUserEmail, userToDeleteEmail);

        verify(friendShipRepository).deleteByUser1EmailOrUser2Email(senderUserEmail, userToDeleteEmail);
        verify(friendShipRepository).deleteByUser1EmailOrUser2Email(userToDeleteEmail, senderUserEmail);
    }
}