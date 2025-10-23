package com.storefront.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testDefaultConstructor() {
        // Act & Assert
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }

    @Test
    void testParameterizedConstructor() {
        // Act
        User user = new User("testuser", "password123");

        // Assert
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertNull(user.getId()); // ID should still be null as it's set by MongoDB
    }

    @Test
    void testSettersAndGetters() {
        // Act
        user.setId("user123");
        user.setUsername("john_doe");
        user.setPassword("securePassword");

        // Assert
        assertEquals("user123", user.getId());
        assertEquals("john_doe", user.getUsername());
        assertEquals("securePassword", user.getPassword());
    }

    @Test
    void testUsernameSetterAndGetter() {
        // Act
        user.setUsername("testuser");

        // Assert
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testPasswordSetterAndGetter() {
        // Act
        user.setPassword("mypassword");

        // Assert
        assertEquals("mypassword", user.getPassword());
    }

    @Test
    void testIdSetterAndGetter() {
        // Act
        user.setId("uniqueId123");

        // Assert
        assertEquals("uniqueId123", user.getId());
    }

    @Test
    void testUserWithNullValues() {
        // Act
        user.setUsername(null);
        user.setPassword(null);
        user.setId(null);

        // Assert
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getId());
    }

    @Test
    void testUserWithEmptyStrings() {
        // Act
        user.setUsername("");
        user.setPassword("");
        user.setId("");

        // Assert
        assertEquals("", user.getUsername());
        assertEquals("", user.getPassword());
        assertEquals("", user.getId());
    }
}
