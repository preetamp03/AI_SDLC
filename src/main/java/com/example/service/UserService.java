package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import com.example.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }
}
```
```java
// src/main/java/com/example/service/OtpService.java