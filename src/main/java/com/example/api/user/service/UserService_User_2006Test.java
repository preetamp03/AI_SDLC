package com.example.api.user.service;

import com.example.api.user.exception.EmailConflictException_User_2005;
import com.example.api.user.model.CreateUserRequest_User_2001;
import com.example.api.user.model.UserEntity_User_2003;
import com.example.api.user.model.UserResponse_User_2002;
import com.example.api.user.repository.UserRepository_User_2004;
import com.example.common.logging.StructuredLogger_API_1001;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService_User_2006.
 */
@ExtendWith(MockitoExtension.class)
class UserService_User_2006Test {

    @Mock
    private UserRepository_User_2004 userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private StructuredLogger_API_1001 logger;

    @InjectMocks
    private UserService_User_2006 userService;

    private CreateUserRequest_User_2001 request;

    @BeforeEach
    void setUp() {
        request = new CreateUserRequest_User_2001();
        request.setEmail("newuser@example.com");
        request.setPassword("StrongPass123@");
        request.setFirstName("New");
        request.setLastName("User");
    }

    /**
     * Tests successful creation of a new user.
     */
    @Test
    void createUser_whenEmailIsUnique_shouldCreateUser() {
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(UserEntity_User_2003.class))).thenAnswer(invocation -> {
            UserEntity_User_2003 user = invocation.getArgument(0);
            user.setId(java.util.UUID.randomUUID());
            user.setCreatedAt(java.time.Instant.now());
            return user;
        });

        UserResponse_User_2002 response = userService.createUser(request);

        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
        assertNotNull(response.getId());
        verify(userRepository, times(1)).save(any(UserEntity_User_2003.class));
    }

    /**
     * Tests that an exception is thrown when the email already exists.
     */
    @Test
    void createUser_whenEmailExists_shouldThrowEmailConflictException() {
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new UserEntity_User_2003()));

        assertThrows(EmailConflictException_User_2005.class, () -> {
            userService.createUser(request);
        });

        verify(userRepository, never()).save(any());
    }
}
```
```java
// src/test/java/com/example/api/product/controller/ProductController_Product_3006Test.java