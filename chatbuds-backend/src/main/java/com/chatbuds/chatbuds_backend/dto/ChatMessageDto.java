package com.chatbuds.chatbuds_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private String sender;
    private String recipient;  // one-to-one chat
    private String content;
    private Instant timestamp;
}