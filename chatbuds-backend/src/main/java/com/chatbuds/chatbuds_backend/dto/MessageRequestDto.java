package com.chatbuds.chatbuds_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequestDto {
    @NotBlank
    private String senderUsername;

    @NotBlank
    private String receiverUsername;

    @NotBlank
    private String content;
}
