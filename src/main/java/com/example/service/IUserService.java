package com.example.service;

import com.example.model.User;
import java.util.UUID;

public interface IUserService {
    /**
     * Finds a user by their phone number.
     * @param phoneNumber The user's phone number.
     * @return The User entity.
     * @throws com.example.exception.ResourceNotFoundException if user not found.
     */
    User findByPhoneNumber(String phoneNumber);

    /**
     * Finds a user by their ID.
     * @param id The user's UUID.
     * @return The User entity.
     * @throws com.example.exception.ResourceNotFoundException if user not found.
     */
    User findById(UUID id);
}