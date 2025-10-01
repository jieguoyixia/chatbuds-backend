package com.chatbuds.chatbuds_backend.repository;

import com.chatbuds.chatbuds_backend.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
    Page<Message> findBySenderAndRecipient(String sender, String recipient, Pageable pageable);
}