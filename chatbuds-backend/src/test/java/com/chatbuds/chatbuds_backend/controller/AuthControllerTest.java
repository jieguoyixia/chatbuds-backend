package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.config.JwtUtil;
import com.chatbuds.chatbuds_backend.dto.*;
import com.chatbuds.chatbuds_backend.model.RefreshToken;
import com.chatbuds.chatbuds_backend.model.User;
import com.chatbuds.chatbuds_backend.service.RefreshTokenService;
import com.chatbuds.chatbuds_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.parser.Parser;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class AuthControllerTest {

    private UserService userService;
    private JwtUtil jwtUtil;
    private RefreshTokenService refreshTokenService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        jwtUtil = mock(JwtUtil.class);
        refreshTokenService = mock(RefreshTokenService.class);
        authController = new AuthController(userService, jwtUtil, refreshTokenService);
    }

    @Test
    void login_ShouldReturnOk_WhenCredentialsAreCorrect() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("user1");
        dto.setPassword("pass");

        User user = new User();
        user.setId("1");
        user.setUsername("user1");
        when(userService.authenticate("user1", "pass")).thenReturn(user);
        when(jwtUtil.generateToken("user1")).thenReturn("access-token");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");
        when(refreshTokenService.createRefreshToken("user1")).thenReturn(refreshToken);

        ResponseEntity<UserResponseDto> response = authController.login(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("user1", response.getBody().getUsername());
        assertEquals("access-token", response.getBody().getAccessToken());
        assertEquals("refresh-token", response.getBody().getRefreshToken());
    }

    @Test
    void login_ShouldReturn401_WhenCredentialsAreWrong() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("user1");
        dto.setPassword("wrong");

        when(userService.authenticate("user1", "wrong")).thenReturn(null);

        ResponseEntity<UserResponseDto> response = authController.login(dto);

        assertEquals(401, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void register_ShouldReturnOk() {
        UserRequestDto dto = new UserRequestDto();
        dto.setUsername("user2");
        dto.setPassword("pass2");

        User user = new User();
        user.setId("2");
        user.setUsername("user2");
        when(userService.register("user2", "pass2")).thenReturn(user);
        when(jwtUtil.generateToken("user2")).thenReturn("access-token2");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token2");
        when(refreshTokenService.createRefreshToken("user2")).thenReturn(refreshToken);

        ResponseEntity<UserResponseDto> response = authController.register(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("user2", response.getBody().getUsername());
        assertEquals("access-token2", response.getBody().getAccessToken());
        assertEquals("refresh-token2", response.getBody().getRefreshToken());
    }

    @Test
    void refreshToken_ShouldReturnOk_WhenTokenValid() {
        TokenRefreshRequestDto request = new TokenRefreshRequestDto();
        request.setRefreshToken("valid-token");

        when(refreshTokenService.isValid("valid-token")).thenReturn(true);
        when(refreshTokenService.getUsernameFromToken("valid-token")).thenReturn("user3");
        when(jwtUtil.generateToken("user3")).thenReturn("new-access-token");
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setToken("new-refresh-token");
        when(refreshTokenService.createRefreshToken("user3")).thenReturn(newRefreshToken);

        ResponseEntity<TokenRefreshResponseDto> response = authController.refreshToken(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("new-access-token", response.getBody().getAccessToken());
        assertEquals("new-refresh-token", response.getBody().getRefreshToken());
    }

    @Test
    void refreshToken_ShouldReturn403_WhenTokenInvalid() {
        TokenRefreshRequestDto request = new TokenRefreshRequestDto();
        request.setRefreshToken("invalid-token");

        when(refreshTokenService.isValid("invalid-token")).thenReturn(false);

        ResponseEntity<TokenRefreshResponseDto> response = authController.refreshToken(request);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void refreshToken_ShouldReturn403_WhenTokenNull() {
        TokenRefreshRequestDto request = new TokenRefreshRequestDto();
        request.setRefreshToken(null);

        ResponseEntity<TokenRefreshResponseDto> response = authController.refreshToken(request);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void logout_ShouldDeleteTokenAndReturnOk() {
        Map<String, String> body = Map.of("refreshToken", "logout-token");

        ResponseEntity<?> response = authController.logout(body);

        verify(refreshTokenService).deleteToken("logout-token");
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).get("message").toString().contains("Logged out"));
    }

    @Test
    void logout_ShouldReturnOk_WhenNoToken() {
        Map<String, String> body = Map.of();

        ResponseEntity<?> response = authController.logout(body);

        verify(refreshTokenService, never()).deleteToken(any());
        assertEquals(200, response.getStatusCodeValue());
    }
}
