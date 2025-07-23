package com.example.security;

public interface IPasswordHasher {
    /**
     * Hashes a raw password.
     * @param password The raw password.
     * @return The hashed password.
     */
    String hash(String password);

    /**
     * Checks if a raw password matches a hashed password.
     * @param rawPassword The raw password to check.
     * @param hashedPassword The stored hashed password.
     * @return True if they match, false otherwise.
     */
    boolean check(String rawPassword, String hashedPassword);
}
```
```java
// src/main/java/com/example/security/BCryptPasswordHasher.java