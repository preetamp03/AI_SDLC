package com.example.api.user.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity representing a user in the database.
 */
@Entity
@Table(name = "users")
@Data
public class UserEntity_User_2003 {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
```
```java
// src/main/java/com/example/api/user/repository/UserRepository_User_2004.java