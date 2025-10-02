package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.model.Message;
import com.chatbuds.chatbuds_backend.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ChatHistoryControllerTest {

    private MessageRepository messageRepository;
    private ChatHistoryController chatHistoryController;

    @BeforeEach
    void setUp() {
        messageRepository = mock(MessageRepository.class);
        chatHistoryController = new ChatHistoryController(messageRepository);
    }

    @Test
    void getHistory_ShouldReturnPageOfMessages() {
        String userA = "alice";
        String userB = "bob";
        int page = 0;
        int size = 2;

        Message msg1 = new Message();
        msg1.setId("1");
        msg1.setSender(userA);
        msg1.setRecipient(userB);
        msg1.setContent("Hello");

        Message msg2 = new Message();
        msg2.setId("2");
        msg2.setSender(userA);
        msg2.setRecipient(userB);
        msg2.setContent("Hi again");

        Page<Message> messagePage = new PageImpl<>(List.of(msg1, msg2));
        when(messageRepository.findBySenderAndRecipient(
                eq(userA), eq(userB), any(PageRequest.class))
        ).thenReturn(messagePage);

        Page<Message> result = chatHistoryController.getHistory(userA, userB, page, size);

        assertEquals(2, result.getContent().size());
        assertEquals("Hello", result.getContent().get(0).getContent());
        verify(messageRepository).findBySenderAndRecipient(eq(userA), eq(userB), any(PageRequest.class));
    }

    @Test
    void getHistory_ShouldUseDefaultPageAndSize() {
        String userA = "alice";
        String userB = "bob";

        when(messageRepository.findBySenderAndRecipient(
                eq(userA), eq(userB), any(PageRequest.class))
        ).thenReturn(Page.empty());

        Page<Message> result = chatHistoryController.getHistory(userA, userB, 0, 20);

        assertNotNull(result);
        verify(messageRepository).findBySenderAndRecipient(eq(userA), eq(userB), any(PageRequest.class));
    }
}