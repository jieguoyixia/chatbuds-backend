package com.chatbuds.chatbuds_backend.dto;

import lombok.Getter;

public class AuthResponse {
    @Getter
    String token;

    public AuthResponse(String token) { this.token = token; }
}
