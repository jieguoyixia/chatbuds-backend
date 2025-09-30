package com.chatbuds.chatbuds_backend.repository;

import com.chatbuds.chatbuds_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}