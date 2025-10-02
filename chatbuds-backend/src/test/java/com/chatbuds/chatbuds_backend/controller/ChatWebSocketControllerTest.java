package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.dto.ChatMessageDto;
import com.chatbuds.chatbuds_backend.model.Message;
import com.chatbuds.chatbuds_backend.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ChatWebSocketControllerTest {

    private MessageRepository messageRepository;
    private SimpMessagingTemplate messagingTemplate;
    private ChatWebSocketController chatWebSocketController;

    @BeforeEach
    void setUp() {
        messageRepository = mock(MessageRepository.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);
        chatWebSocketController = new ChatWebSocketController(messagingTemplate, messageRepository);
    }

    @Test
    void sendMessage_ShouldSaveAndBroadcast() {
        ChatMessageDto message = new ChatMessageDto();
        message.setSender("alice");
        message.setRecipient("bob");
        message.setContent("Hello!");

        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        chatWebSocketController.sendMessage(message);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(captor.capture());
        Message saved = captor.getValue();
        assertEquals("alice", saved.getSender());
        assertEquals("bob", saved.getRecipient());
        assertEquals("Hello!", saved.getContent());

        verify(messagingTemplate).convertAndSend(eq("/topic/messages/bob"), eq(message));
    }
}