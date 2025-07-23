package com.example.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements IPasswordHasher {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * {@inheritDoc}
     */
    @Override
    public String hash(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
```
```java
// src/main/java/com/example/security/ITokenService.java