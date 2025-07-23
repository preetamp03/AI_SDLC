package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Finds a user by their phone number.
     * @param phoneNumber The user's phone number.
     * @return An optional containing the user if found.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
}