package com.chatbuds.chatbuds_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;

@Data
@AllArgsConstructor
public class MessageResponseDto {
    private String id;
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private Instant timestamp;
}