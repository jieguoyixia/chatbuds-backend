package com.chatbuds.chatbuds_backend.dto;

import lombok.Data;

@Data
public class TokenRefreshRequestDto {
    private String refreshToken;
}