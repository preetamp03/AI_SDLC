package com.example.api.user.repository;

import com.example.api.user.model.UserEntity_User_2003;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for UserEntity_User_2003.
 */
@Repository
public interface UserRepository_User_2004 extends JpaRepository<UserEntity_User_2003, UUID> {

    /**
     * Finds a user by their email address.
     * @param email The email to search for.
     * @return An Optional containing the user if found.
     */
    Optional<UserEntity_User_2003> findByEmail(String email);
}
```
```java
// src/main/java/com/example/api/user/exception/EmailConflictException_User_2005.java