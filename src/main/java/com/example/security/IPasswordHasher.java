package com.example.security;

public interface IPasswordHasher {
    /**
     * Hashes a password.
     * @param rawPassword The raw password.
     * @return The hashed password.
     */
    String hash(String rawPassword);

    /**
     * Compares a raw password with a hashed one.
     * @param rawPassword The raw password.
     * @param hashedPassword The hashed password.
     * @return true if they match, false otherwise.
     */
    boolean matches(String rawPassword, String hashedPassword);
}