package com.chatbuds.chatbuds_backend.service;

import com.chatbuds.chatbuds_backend.model.RefreshToken;
import com.chatbuds.chatbuds_backend.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

class RefreshTokenServiceTest {

    private RefreshTokenRepository refreshTokenRepository;
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        refreshTokenService = new RefreshTokenService(refreshTokenRepository);
        // 反射设置 refreshExpirationMs
        try {
            var field = RefreshTokenService.class.getDeclaredField("refreshExpirationMs");
            field.setAccessible(true);
            field.set(refreshTokenService, 1000L * 60 * 60); // 1小时
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createRefreshToken_ShouldDeleteOldAndSaveNew() {
        String username = "user1";
        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken token = refreshTokenService.createRefreshToken(username);

        verify(refreshTokenRepository).deleteByUsername(username);
        assertEquals(username, token.getUsername());
        assertNotNull(token.getToken());
        assertTrue(token.getExpiryDate().isAfter(Instant.now()));
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void isValid_ShouldReturnTrue_WhenTokenExistsAndNotExpired() {
        String tokenStr = UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken();
        token.setToken(tokenStr);
        token.setExpiryDate(Instant.now().plusSeconds(60));
        when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(token));

        assertTrue(refreshTokenService.isValid(tokenStr));
    }

    @Test
    void isValid_ShouldReturnFalse_WhenTokenNotFound() {
        when(refreshTokenRepository.findByToken("notfound")).thenReturn(Optional.empty());
        assertFalse(refreshTokenService.isValid("notfound"));
    }

    @Test
    void isValid_ShouldReturnFalse_WhenTokenExpired() {
        String tokenStr = UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken();
        token.setToken(tokenStr);
        token.setExpiryDate(Instant.now().minusSeconds(60));
        when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(token));

        assertFalse(refreshTokenService.isValid(tokenStr));
    }

    @Test
    void getUsernameFromToken_ShouldReturnUsername_WhenTokenExists() {
        String tokenStr = UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken();
        token.setToken(tokenStr);
        token.setUsername("user2");
        when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(token));

        assertEquals("user2", refreshTokenService.getUsernameFromToken(tokenStr));
    }

    @Test
    void getUsernameFromToken_ShouldReturnNull_WhenTokenNotFound() {
        when(refreshTokenRepository.findByToken("notfound")).thenReturn(Optional.empty());
        assertNull(refreshTokenService.getUsernameFromToken("notfound"));
    }

    @Test
    void deleteToken_ShouldDelete_WhenTokenExists() {
        String tokenStr = UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken();
        token.setToken(tokenStr);
        when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(token));

        refreshTokenService.deleteToken(tokenStr);

        verify(refreshTokenRepository).delete(token);
    }

    @Test
    void deleteToken_ShouldNotDelete_WhenTokenNotFound() {
        when(refreshTokenRepository.findByToken("notfound")).thenReturn(Optional.empty());

        refreshTokenService.deleteToken("notfound");

        verify(refreshTokenRepository, never()).delete(any());
    }
}
