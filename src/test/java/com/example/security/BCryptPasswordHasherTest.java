package com.example.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordHasherTest {

    private IPasswordHasher passwordHasher;

    @BeforeEach
    void setUp() {
        passwordHasher = new BCryptPasswordHasher();
    }

    @Test
    void hash_shouldReturnNonEmptyString() {
        String hashedPassword = passwordHasher.hash("password123");
        assertNotNull(hashedPassword);
        assertNotEquals("password123", hashedPassword);
    }

    @Test
    void check_shouldReturnTrueForMatchingPasswords() {
        String rawPassword = "mySecurePassword";
        String hashedPassword = passwordHasher.hash(rawPassword);
        assertTrue(passwordHasher.check(rawPassword, hashedPassword));
    }

    @Test
    void check_shouldReturnFalseForNonMatchingPasswords() {
        String hashedPassword = passwordHasher.hash("correctPassword");
        assertFalse(passwordHasher.check("wrongPassword", hashedPassword));
    }
}