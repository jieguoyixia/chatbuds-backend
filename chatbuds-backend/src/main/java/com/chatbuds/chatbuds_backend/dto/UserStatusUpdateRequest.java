package com.chatbuds.chatbuds_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserStatusUpdateRequest {
    private boolean online;
}