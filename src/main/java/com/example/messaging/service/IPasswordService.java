package com.example.messaging.service;

public interface IPasswordService {
    /**
     * Hashes a plain-text password.
     * @param plainText The plain-text password.
     * @return The hashed password string.
     */
    String hashPassword(String plainText);

    /**
     * Compares a plain-text password with a hash.
     * @param plainText The plain-text password to check.
     * @param hash The stored hash to compare against.
     * @return True if the password matches the hash, false otherwise.
     */
    boolean comparePassword(String plainText, String hash);
}