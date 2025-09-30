package com.chatbuds.chatbuds_backend.service;

import com.chatbuds.chatbuds_backend.model.Message;
import com.chatbuds.chatbuds_backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final MessageRepository messageRepository;

    public ChatService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Message msg) {
        return messageRepository.save(msg);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByTimestampAsc();
    }
}