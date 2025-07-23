package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    /**
     * Finds a user by their phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the user if found.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
}
```
```java
// src/main/java/com/example/security/JwtAuthenticationFilter.java