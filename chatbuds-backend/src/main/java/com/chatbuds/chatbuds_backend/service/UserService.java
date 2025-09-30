package com.chatbuds.chatbuds_backend.service;

import com.chatbuds.chatbuds_backend.model.User;
import com.chatbuds.chatbuds_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String password) {
        String hashed = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashed);
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElse(null);
    }
}