package com.chatbuds.chatbuds_backend.service;

import com.chatbuds.chatbuds_backend.model.User;
import com.chatbuds.chatbuds_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void register_ShouldSaveUser_WhenUsernameNotExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.register("testuser", "password123");

        assertEquals("testuser", user.getUsername());
        assertNotEquals("password123", user.getPassword()); // 密码应加密
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register("testuser", "password123");
        });
    }

    @Test
    void authenticate_ShouldReturnUser_WhenCredentialsAreCorrect() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("password123"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User result = userService.authenticate("testuser", "password123");
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void authenticate_ShouldReturnNull_WhenCredentialsAreIncorrect() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("password123"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User result = userService.authenticate("testuser", "wrongpassword");
        assertNull(result);
    }

    @Test
    void authenticate_ShouldReturnNull_WhenUserNotFound() {
        when(userRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        User result = userService.authenticate("nouser", "password123");
        assertNull(result);
    }
}