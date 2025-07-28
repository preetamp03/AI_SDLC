package com.example.messaging.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordServiceTest {

    private PasswordService passwordService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        passwordService = new PasswordService(passwordEncoder);
    }

    /**
     * Tests that a password can be hashed and then successfully compared.
     */
    @Test
    void hashAndComparePassword_shouldMatch() {
        String plainPassword = "mySecretPassword123";
        String hashedPassword = passwordService.hashPassword(plainPassword);

        assertThat(hashedPassword).isNotNull().isNotEqualTo(plainPassword);
        
        boolean matches = passwordService.comparePassword(plainPassword, hashedPassword);
        assertThat(matches).isTrue();
    }

    /**
     * Tests that comparison fails for an incorrect password.
     */
    @Test
    void comparePassword_withWrongPassword_shouldNotMatch() {
        String plainPassword = "mySecretPassword123";
        String wrongPassword = "wrongPassword";
        String hashedPassword = passwordService.hashPassword(plainPassword);

        boolean matches = passwordService.comparePassword(wrongPassword, hashedPassword);
        assertThat(matches).isFalse();
    }
}