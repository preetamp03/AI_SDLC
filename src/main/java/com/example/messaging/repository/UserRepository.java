package com.example.messaging.repository;

import com.example.messaging.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Finds a user by their phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the user if found.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
}