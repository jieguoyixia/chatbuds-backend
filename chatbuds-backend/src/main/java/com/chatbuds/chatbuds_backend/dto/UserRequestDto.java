package com.chatbuds.chatbuds_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password must be at least 8 characters")
    private String password;
}