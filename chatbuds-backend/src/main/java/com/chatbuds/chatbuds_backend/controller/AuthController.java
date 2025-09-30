package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.config.JwtUtil;
import com.chatbuds.chatbuds_backend.dto.*;
import com.chatbuds.chatbuds_backend.model.RefreshToken;
import com.chatbuds.chatbuds_backend.model.User;
import com.chatbuds.chatbuds_backend.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserService userService, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        User u = userService.authenticate(dto.getUsername(), dto.getPassword());
        if (u == null) {
            return ResponseEntity.status(401).build();
        }

        String accessToken = jwtUtil.generateToken(u.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(u.getUsername());

        UserResponseDto response = new UserResponseDto(
                u.getId(),
                u.getUsername(),
                accessToken,
                refreshToken.getToken()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto dto) {
        User user = userService.register(dto.getUsername(), dto.getPassword());

        String accessToken = jwtUtil.generateToken(user.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        UserResponseDto response = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                accessToken,
                refreshToken.getToken()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(@RequestBody TokenRefreshRequestDto request) {
        String refreshTokenStr = request.getRefreshToken();
        if (refreshTokenStr == null || !refreshTokenService.isValid(refreshTokenStr)) {
            return ResponseEntity.status(403).build();
        }

        String username = refreshTokenService.getUsernameFromToken(refreshTokenStr);
        String newAccessToken = jwtUtil.generateToken(username);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(username);

        return ResponseEntity.ok(new TokenRefreshResponseDto(newAccessToken, newRefreshToken.getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        String refreshTokenStr = body.get("refreshToken");
        if (refreshTokenStr != null) refreshTokenService.deleteToken(refreshTokenStr);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully. Discard your access token."));
    }
}