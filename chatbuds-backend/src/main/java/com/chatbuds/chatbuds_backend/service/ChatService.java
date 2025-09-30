package com.chatbuds.chatbuds_backend.service;

import com.chatbuds.chatbuds_backend.model.Message;
import com.chatbuds.chatbuds_backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatService {
    private final MessageRepository messageRepository;

    public ChatService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Message msg) {
        return messageRepository.save(msg);
    }

    /**
     * Get the full 1-to-1 chat between two usernames, sorted by timestamp ascending.
     */
    public List<Message> getChatBetween(String userA, String userB) {
        List<Message> aToB = messageRepository.findBySenderUsernameAndReceiverUsernameOrderByTimestampAsc(userA, userB);
        List<Message> bToA = messageRepository.findBySenderUsernameAndReceiverUsernameOrderByTimestampAsc(userB, userA);
        return Stream.concat(aToB.stream(), bToA.stream())
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.toList());
    }
}