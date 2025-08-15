package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.domain.User;
import com.chatbuds.chatbuds_backend.dto.LoginRequest;
import com.chatbuds.chatbuds_backend.dto.RegisterRequest;
import com.chatbuds.chatbuds_backend.repository.UserRepository;
import com.chatbuds.chatbuds_backend.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = new User(req.getUsername(), req.getEmail(), req.getPassword(), false);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepo.save(user);

        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername());
        return ResponseEntity.ok().body(token);
    }
}