package com.example.socialmediaapi.controller;

import com.example.socialmediaapi.dto.general.ResponseDto;
import com.example.socialmediaapi.dto.message.ContentChatFriendsDto;
import com.example.socialmediaapi.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class MessageController {
    private final MessageService messageService;


    @PostMapping("/send")
    @Operation(summary = "[US 4.1] Send message from one friend to other",
            description = "This API is used to send messages between friends.")
    public ResponseEntity<ResponseDto<String>> sendMessage(@RequestParam @Size(min = 8) @Email String senderEmail,
                                                           @RequestParam @Size(min = 8) @Email String receiverEmail, @RequestBody String content) {
        messageService.sendMessage(senderEmail,receiverEmail,content);
        ResponseDto<String> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                String.format("Message was send from %s to %s", senderEmail, receiverEmail));
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/get-friends-chat")
    @Operation(summary = "[US 4.2] Get chat between two friends by email",
            description = "This API is used to get chat between two friends.")
    public ResponseEntity<ResponseDto<List<ContentChatFriendsDto>>> getFriendsChat(@RequestParam @Email @Size(min = 8) String emailSender,
                                                                                   @RequestParam @Email @Size(min = 8) String emailReceiver) {
        List<ContentChatFriendsDto> messageDtos = messageService.getChatBetweenUsers(emailSender,emailReceiver);
        ResponseDto<List<ContentChatFriendsDto>> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                messageDtos);
        return ResponseEntity.ok(responseDto);
    }

}
