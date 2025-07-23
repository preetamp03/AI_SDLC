package com.example.service;

import com.example.model.User;

import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    /**
     * Finds a user by their phone number.
     * @param phoneNumber The user's phone number.
     * @return An Optional containing the user if found.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    /**
     * Finds a user by their ID.
     * @param id The user's UUID.
     * @return The User object.
     * @throws com.example.exception.ResourceNotFoundException if user is not found.
     */
    User findById(UUID id);
}