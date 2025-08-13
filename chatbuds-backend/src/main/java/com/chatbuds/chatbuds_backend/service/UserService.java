package com.chatbuds.chatbuds_backend.service;

import com.chatbuds.chatbuds_backend.domain.User;
import com.chatbuds.chatbuds_backend.repository.UserRepository;
import com.chatbuds.chatbuds_backend.repository.UserStatusRedisRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserStatusRedisRepository redisRepository;

    public UserService(UserRepository userRepository, UserStatusRedisRepository redisRepository) {
        this.userRepository = userRepository;
        this.redisRepository = redisRepository;
    }

    public User registerUser(String username, String email) {
        User user = new User(username, email, false);
        return userRepository.save(user);
    }

    public void setOnline(String userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setOnline(true);
            userRepository.save(user);
            redisRepository.markUserOnline(user.getId());
        });
    }

    public void setOffline(String userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setOnline(false);
            userRepository.save(user);
            redisRepository.markUserOffline(user.getId());
        });
    }

    public List<User> getOnlineUsers() {
        Set<Object> ids = redisRepository.getOnlineUsers();
        List<String> stringIds = ids.stream().map(Object::toString).collect(Collectors.toList());
        return userRepository.findAllById(stringIds);
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
