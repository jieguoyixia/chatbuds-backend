package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.domain.User;
import com.chatbuds.chatbuds_backend.dto.UserStatusUpdateRequest;
import com.chatbuds.chatbuds_backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Register new user
    @PostMapping
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user.getUsername(), user.getEmail());
    }

    // Get user by ID
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUserById(id);
    }

    // Update user's online status
    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable String id, @RequestBody UserStatusUpdateRequest request) {
        if (request.isOnline()) {
            userService.setOnline(id);
        } else {
            userService.setOffline(id);
        }
    }

    // Get all users or filter by online status
    @GetMapping
    public List<User> getUsers(@RequestParam(value = "online", required = false) Boolean online) {
        if (Boolean.TRUE.equals(online)) {
            return userService.getOnlineUsers();
        }
        return userService.getAllUsers();
    }

    // delete user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}

