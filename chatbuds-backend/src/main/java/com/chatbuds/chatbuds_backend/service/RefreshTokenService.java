package com.chatbuds.chatbuds_backend.service;

import com.chatbuds.chatbuds_backend.model.RefreshToken;
import com.chatbuds.chatbuds_backend.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration-ms:604800000}") // default 7 days
    private long refreshExpirationMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(String username) {
        RefreshToken token = new RefreshToken();
        token.setUsername(username);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));
        return refreshTokenRepository.save(token);
    }

    public boolean isValid(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(rt -> rt.getExpiryDate().isAfter(Instant.now()))
                .isPresent();
    }

    public String getUsernameFromToken(String token) {
        return refreshTokenRepository.findByToken(token).map(RefreshToken::getUsername).orElse(null);
    }

    public void deleteToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
