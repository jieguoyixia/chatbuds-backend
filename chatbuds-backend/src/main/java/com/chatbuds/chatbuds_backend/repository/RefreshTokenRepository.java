package com.chatbuds.chatbuds_backend.repository;

import com.chatbuds.chatbuds_backend.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUsername(String username);
}