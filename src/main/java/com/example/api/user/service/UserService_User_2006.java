package com.example.api.user.service;

import com.example.api.user.exception.EmailConflictException_User_2005;
import com.example.api.user.model.CreateUserRequest_User_2001;
import com.example.api.user.model.UserEntity_User_2003;
import com.example.api.user.model.UserResponse_User_2002;
import com.example.api.user.repository.UserRepository_User_2004;
import com.example.common.logging.StructuredLogger_API_1001;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer containing business logic for user management.
 */
@Service
@RequiredArgsConstructor
public class UserService_User_2006 {

    private final UserRepository_User_2004 userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StructuredLogger_API_1001 logger;

    /**
     * Creates a new user account.
     * @param request The request DTO containing user information.
     * @return A DTO with the created user's public data.
     * @throws EmailConflictException_User_2005 if the email is already in use.
     */
    @Transactional
    public UserResponse_User_2002 createUser(CreateUserRequest_User_2001 request) {
        final long startTime = System.currentTimeMillis();
        final String methodName = "createUser";
        logger.logStart(this.getClass().getSimpleName(), methodName);

        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new EmailConflictException_User_2005("An account with this email address already exists.");
        });

        UserEntity_User_2003 newUser = new UserEntity_User_2003();
        newUser.setEmail(request.getEmail());
        newUser.setHashedPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());

        UserEntity_User_2003 savedUser = userRepository.save(newUser);
        logger.logInfo("User created successfully", "userId", savedUser.getId());

        UserResponse_User_2002 response = UserResponse_User_2002.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .createdAt(savedUser.getCreatedAt())
                .build();
        
        logger.logEnd(this.getClass().getSimpleName(), methodName, startTime);
        return response;
    }
}
```
```java
// src/main/java/com/example/api/user/controller/UserController_User_2007.java