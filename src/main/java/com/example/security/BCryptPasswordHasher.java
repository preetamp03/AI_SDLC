package com.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BCryptPasswordHasher implements IPasswordHasher {

    private final PasswordEncoder passwordEncoder;

    /**
     * Hashes a raw password.
     * @param rawPassword The password to hash.
     * @return The hashed password.
     */
    @Override
    public String hash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Checks if a raw password matches a hashed password.
     * @param rawPassword The raw password.
     * @param hashedPassword The hashed password.
     * @return true if they match, false otherwise.
     */
    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}