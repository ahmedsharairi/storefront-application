package com.storefront.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "mySecretKeyForTestingPurposesThisNeedsToBeAtLeast256Bits";
    private final int testExpiration = 86400000; // 24 hours in milliseconds
    private final String testUsername = "testuser";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Use reflection to set private fields for testing
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", testExpiration);
        // Initialize the key
        jwtUtil.init();
    }

    @Test
    void generateToken_Success() {
        // Act
        String token = jwtUtil.generateToken(testUsername);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
        // JWT tokens have 3 parts separated by dots
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void getUsernameFromToken_Success() {
        // Arrange
        String token = jwtUtil.generateToken(testUsername);

        // Act
        String extractedUsername = jwtUtil.getUsernameFromToken(token);

        // Assert
        assertEquals(testUsername, extractedUsername);
    }

    @Test
    void validateJwtToken_ValidToken_ReturnsTrue() {
        // Arrange
        String token = jwtUtil.generateToken(testUsername);

        // Act
        boolean isValid = jwtUtil.validateJwtToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateJwtToken_InvalidToken_ReturnsFalse() {
        // Arrange
        String invalidToken = "invalid.jwt.token";

        // Act
        boolean isValid = jwtUtil.validateJwtToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_MalformedToken_ReturnsFalse() {
        // Arrange
        String malformedToken = "malformed-token-without-dots";

        // Act
        boolean isValid = jwtUtil.validateJwtToken(malformedToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_EmptyToken_ReturnsFalse() {
        // Arrange
        String emptyToken = "";

        // Act
        boolean isValid = jwtUtil.validateJwtToken(emptyToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_NullToken_ReturnsFalse() {
        // Act
        boolean isValid = jwtUtil.validateJwtToken(null);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void generateToken_DifferentUsers_ProduceDifferentTokens() {
        // Act
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");

        // Assert
        assertNotEquals(token1, token2);
    }

    @Test
    void generateToken_SameUser_ProduceDifferentTokens() throws InterruptedException {
        // Act
        String token1 = jwtUtil.generateToken(testUsername);
        Thread.sleep(1000); // Wait 1 second to ensure different timestamps
        String token2 = jwtUtil.generateToken(testUsername);

        // Assert
        assertNotEquals(token1, token2); // Different because of different issue times
    }

    @Test
    void getUsernameFromToken_InvalidToken_ThrowsException() {
        // Arrange
        String invalidToken = "invalid.jwt.token";

        // Act & Assert
        assertThrows(Exception.class, () -> {
            jwtUtil.getUsernameFromToken(invalidToken);
        });
    }
}
