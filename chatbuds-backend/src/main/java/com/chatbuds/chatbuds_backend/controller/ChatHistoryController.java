package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.model.Message;
import com.chatbuds.chatbuds_backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final MessageRepository messageRepository;

    @GetMapping("/history")
    public Page<Message> getHistory(
            @RequestParam String userA,
            @RequestParam String userB,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return messageRepository.findBySenderAndRecipient(userA, userB, PageRequest.of(page, size));
    }
}