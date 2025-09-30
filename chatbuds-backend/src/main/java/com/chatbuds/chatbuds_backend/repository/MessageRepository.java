package com.chatbuds.chatbuds_backend.repository;

import com.chatbuds.chatbuds_backend.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findAllByOrderByTimestampAsc();
}
