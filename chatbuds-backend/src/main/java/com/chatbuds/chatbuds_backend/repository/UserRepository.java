package com.chatbuds.chatbuds_backend.repository;

import com.chatbuds.chatbuds_backend.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByOnlineTrue();
}

