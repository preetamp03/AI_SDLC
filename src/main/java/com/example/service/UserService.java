package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The user's phone number.
     * @return The found User entity.
     * @throws ResourceNotFoundException if the user is not found.
     */
    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with phone number: " + phoneNumber));
    }

    /**
     * Finds a user by their unique ID.
     * @param id The user's UUID.
     * @return The found User entity.
     * @throws ResourceNotFoundException if the user is not found.
     */
    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }
}