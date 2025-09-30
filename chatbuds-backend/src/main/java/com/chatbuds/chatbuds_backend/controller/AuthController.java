package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.config.JwtUtil;
import com.chatbuds.chatbuds_backend.dto.LoginRequestDto;
import com.chatbuds.chatbuds_backend.dto.UserRequestDto;
import com.chatbuds.chatbuds_backend.dto.UserResponseDto;
import com.chatbuds.chatbuds_backend.model.User;
import com.chatbuds.chatbuds_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto dto) {
        User u = userService.register(dto.getUsername(), dto.getPassword());
        String token = jwtUtil.generateToken(u.getUsername());
        return ResponseEntity.ok(new UserResponseDto(u.getId(), u.getUsername(), token));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        User u = userService.authenticate(dto.getUsername(), dto.getPassword());
        if (u == null) {
            return ResponseEntity.status(401).build();
        }
        String token = jwtUtil.generateToken(u.getUsername());
        return ResponseEntity.ok(new UserResponseDto(u.getId(), u.getUsername(), token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out successfully. Please discard your token."));
    }

}