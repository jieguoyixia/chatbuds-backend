package com.chatbuds.chatbuds_backend.controller;

import com.chatbuds.chatbuds_backend.config.JwtUtil;
import com.chatbuds.chatbuds_backend.model.User;
import com.chatbuds.chatbuds_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
        User user = userService.register(body.get("username"), body.get("password"));
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        User user = userService.login(body.get("username"), body.get("password"));
        if (user == null) return ResponseEntity.status(401).body("Invalid credentials");
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

}