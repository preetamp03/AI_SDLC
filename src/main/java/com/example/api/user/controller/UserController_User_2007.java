package com.example.api.user.controller;

import com.example.api.user.model.CreateUserRequest_User_2001;
import com.example.api.user.model.UserResponse_User_2002;
import com.example.api.user.service.UserService_User_2006;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController_User_2007 {

    private final UserService_User_2006 userService;

    /**
     * Handles the creation of a new user account.
     * @param createUserRequest The request body containing user data.
     * @return A response entity with the created user's data and a 201 status.
     */
    @PostMapping
    public ResponseEntity<UserResponse_User_2002> createUserAccount(@Valid @RequestBody CreateUserRequest_User_2001 createUserRequest) {
        UserResponse_User_2002 createdUser = userService.createUser(createUserRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
```
```java
// src/main/java/com/example/api/product/model/Price_Product_3001.java