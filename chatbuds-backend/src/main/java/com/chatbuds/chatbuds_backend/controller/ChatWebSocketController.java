package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.dto.ChatMessageDto;
import com.chatbuds.chatbuds_backend.model.Message;
import com.chatbuds.chatbuds_backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDto chatMessageDto) {
        chatMessageDto.setTimestamp(Instant.now());

        // Save to MongoDB
        Message message = new Message();
        message.setSender(chatMessageDto.getSender());
        message.setRecipient(chatMessageDto.getRecipient());
        message.setContent(chatMessageDto.getContent());
        message.setTimestamp(chatMessageDto.getTimestamp());
        messageRepository.save(message);

        // Broadcast to recipient (topic unique per user)
        messagingTemplate.convertAndSend("/topic/messages/" + chatMessageDto.getRecipient(), chatMessageDto);
    }
}