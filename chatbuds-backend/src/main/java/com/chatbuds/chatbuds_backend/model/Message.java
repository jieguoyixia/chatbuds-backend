package com.chatbuds.chatbuds_backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private Instant timestamp = Instant.now();
}

