package com.storefront.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storefront.backend.models.User;
import com.storefront.backend.security.JwtUtil;
import com.storefront.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "password123");
    }

    @Test
    void registerUser_Success() throws Exception {
        // Arrange
        doNothing().when(userService).createUser(any(User.class));

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());

        verify(userService).createUser(any(User.class));
    }

    @Test
    void registerUser_ThrowsException_WhenUserServiceFails() throws Exception {
        // Arrange
        doThrow(new RuntimeException("User already exists")).when(userService).createUser(any(User.class));

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void loginUser_Success_ReturnsToken() throws Exception {
        // Arrange
        String expectedToken = "jwt-token-123";
        when(userService.findUser(any(User.class))).thenReturn(expectedToken);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedToken));

        verify(userService).findUser(any(User.class));
    }

    @Test
    void loginUser_InvalidCredentials_ThrowsException() throws Exception {
        // Arrange
        when(userService.findUser(any(User.class))).thenThrow(new RuntimeException("Invalid login!"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void verifyToken_ValidToken_ReturnsTrue() throws Exception {
        // Arrange
        String validToken = "valid-jwt-token";
        when(jwtUtil.validateJwtToken(validToken)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"" + validToken + "\""))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(jwtUtil).validateJwtToken(validToken);
    }

    @Test
    void verifyToken_InvalidToken_ReturnsFalse() throws Exception {
        // Arrange
        String invalidToken = "invalid-jwt-token";
        when(jwtUtil.validateJwtToken(invalidToken)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"" + invalidToken + "\""))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(jwtUtil).validateJwtToken(invalidToken);
    }
}
