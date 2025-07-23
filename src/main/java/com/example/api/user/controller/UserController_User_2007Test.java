package com.example.api.user.controller;

import com.example.api.user.exception.EmailConflictException_User_2005;
import com.example.api.user.model.CreateUserRequest_User_2001;
import com.example.api.user.model.UserResponse_User_2002;
import com.example.api.user.service.UserService_User_2006;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.common.exception.GlobalExceptionHandler_Common_1007;
import com.example.common.logging.Log4j2StructuredLogger_API_1002;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for UserController_User_2007.
 */
@WebMvcTest(UserController_User_2007.class)
@Import({GlobalExceptionHandler_Common_1007.class, Log4j2StructuredLogger_API_1002.class})
class UserController_User_2007Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService_User_2006 userService;

    private CreateUserRequest_User_2001 validRequest;
    private UserResponse_User_2002 userResponse;

    @BeforeEach
    void setUp() {
        validRequest = new CreateUserRequest_User_2001();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("ValidPass123@");
        validRequest.setFirstName("Test");
        validRequest.setLastName("User");

        userResponse = UserResponse_User_2002.builder()
            .id(UUID.randomUUID())
            .email("test@example.com")
            .firstName("Test")
            .lastName("User")
            .createdAt(Instant.now())
            .build();
    }

    /**
     * Tests successful user creation (201 Created).
     */
    @Test
    void createUserAccount_whenValidRequest_shouldReturn201Created() throws Exception {
        when(userService.createUser(any(CreateUserRequest_User_2001.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(userResponse.getId().toString()))
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    /**
     * Tests user creation with invalid email format (400 Bad Request).
     */
    @Test
    void createUserAccount_whenInvalidEmail_shouldReturn400BadRequest() throws Exception {
        validRequest.setEmail("not-an-email");

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"))
            .andExpect(jsonPath("$.invalidFields.email").value("Email should be valid."));
    }
    
    /**
     * Tests user creation with a weak password (400 Bad Request).
     */
    @Test
    void createUserAccount_whenWeakPassword_shouldReturn400BadRequest() throws Exception {
        validRequest.setPassword("short");

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"))
            .andExpect(jsonPath("$.invalidFields.password").exists());
    }


    /**
     * Tests user creation when email already exists (409 Conflict).
     */
    @Test
    void createUserAccount_whenEmailExists_shouldReturn409Conflict() throws Exception {
        when(userService.createUser(any(CreateUserRequest_User_2001.class)))
            .thenThrow(new EmailConflictException_User_2005("Email already exists."));

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.errorCode").value("EMAIL_EXISTS"));
    }
}
```
```java
// src/test/java/com/example/api/user/service/UserService_User_2006Test.java