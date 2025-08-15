package com.chatbuds.chatbuds_backend.repository;

import com.chatbuds.chatbuds_backend.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    List<User> findByOnlineTrue();
}