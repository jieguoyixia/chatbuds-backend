package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.dto.MessageRequestDto;
import com.chatbuds.chatbuds_backend.dto.MessageResponseDto;
import com.chatbuds.chatbuds_backend.model.Message;
import com.chatbuds.chatbuds_backend.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final ChatService chatService;

    public MessageController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public ResponseEntity<MessageResponseDto> send(@Valid @RequestBody MessageRequestDto dto) {
        // ensure the authenticated username matches the senderUsername in DTO
        String authUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!authUsername.equals(dto.getSenderUsername())) {
            return ResponseEntity.status(403).build();
        }

        Message message = new Message();
        message.setSenderUsername(dto.getSenderUsername());
        message.setReceiverUsername(dto.getReceiverUsername());
        message.setContent(dto.getContent());
        Message saved = chatService.saveMessage(message);
        MessageResponseDto resp = new MessageResponseDto(saved.getId(), saved.getSenderUsername(), saved.getReceiverUsername(), saved.getContent(), saved.getTimestamp());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/chat")
    public ResponseEntity<List<MessageResponseDto>> getChat(@RequestParam String userA, @RequestParam String userB) {
        String authUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!authUsername.equals(userA) && !authUsername.equals(userB)) {
            return ResponseEntity.status(403).build();
        }

        List<MessageResponseDto> list = chatService.getChatBetween(userA, userB).stream()
                .map(m -> new MessageResponseDto(m.getId(), m.getSenderUsername(), m.getReceiverUsername(), m.getContent(), m.getTimestamp()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}