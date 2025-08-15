package com.chatbuds.chatbuds_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RegisterRequest {
    @NotBlank @Setter private String username;
    @Email @NotBlank @Setter private String email;
    @NotBlank @Setter private String password;
}