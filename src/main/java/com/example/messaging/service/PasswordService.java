package com.example.messaging.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService implements IPasswordService {

    private final PasswordEncoder passwordEncoder;

    /**
     * Hashes a plain-text password using the configured PasswordEncoder.
     * @param plainText The plain-text password.
     * @return The hashed password string.
     */
    @Override
    public String hashPassword(String plainText) {
        return passwordEncoder.encode(plainText);
    }

    /**
     * Compares a plain-text password with a hash.
     * @param plainText The plain-text password to check.
     * @param hash The stored hash to compare against.
     * @return True if the password matches the hash, false otherwise.
     */
    @Override
    public boolean comparePassword(String plainText, String hash) {
        return passwordEncoder.matches(plainText, hash);
    }
}