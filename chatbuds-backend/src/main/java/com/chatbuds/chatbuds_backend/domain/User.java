package com.chatbuds.chatbuds_backend.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Setter
    private String username;
    @Setter
    private String email;
    @Setter
    private boolean online;

    public User() {}

    public User(String username, String email, boolean online) {
        this.username = username;
        this.email = email;
        this.online = online;
    }
}
