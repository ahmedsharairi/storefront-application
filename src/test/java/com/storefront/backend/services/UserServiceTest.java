package com.storefront.backend.services;

import com.storefront.backend.models.User;
import com.storefront.backend.repositories.UserRepository;
import com.storefront.backend.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User savedUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "password123");
        savedUser = new User("testuser", "encodedPassword");
        savedUser.setId("user123");
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        userService.createUser(testUser);

        // Assert
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(testUser);
        assertEquals("encodedPassword", testUser.getPassword());
    }

    @Test
    void createUser_ThrowsException_WhenRepositoryFails() {
        // Arrange
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(testUser);
        });

        assertTrue(exception.getMessage().contains("Unable to create user:"));
    }

    @Test
    void findUser_Success_ReturnsToken() {
        // Arrange
        User loginRequest = new User("testuser", "password123");
        List<User> users = Arrays.asList(savedUser);

        when(userRepository.findAll()).thenReturn(users);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testuser")).thenReturn("jwt-token-123");

        // Act
        String token = userService.findUser(loginRequest);

        // Assert
        assertEquals("jwt-token-123", token);
        verify(userRepository).findAll();
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtil).generateToken("testuser");
    }

    @Test
    void findUser_ThrowsException_WhenUserNotFound() {
        // Arrange
        User loginRequest = new User("nonexistent", "password123");
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findUser(loginRequest);
        });

        assertEquals("Invalid login!", exception.getMessage());
    }

    @Test
    void findUser_ThrowsException_WhenPasswordIncorrect() {
        // Arrange
        User loginRequest = new User("testuser", "wrongpassword");
        List<User> users = Arrays.asList(savedUser);

        when(userRepository.findAll()).thenReturn(users);
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findUser(loginRequest);
        });

        assertEquals("Invalid login!", exception.getMessage());
    }
}
