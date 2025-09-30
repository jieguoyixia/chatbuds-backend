package com.chatbuds.chatbuds_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private String id;
    private String username;
    private String token;
}
