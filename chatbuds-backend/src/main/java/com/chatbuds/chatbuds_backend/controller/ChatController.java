package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.model.Message;
import com.chatbuds.chatbuds_backend.service.ChatService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message msg) {
        return chatService.saveMessage(msg);
    }

    @GetMapping("/history")
    public List<Message> getMessages() {
        return chatService.getAllMessages();
    }
}