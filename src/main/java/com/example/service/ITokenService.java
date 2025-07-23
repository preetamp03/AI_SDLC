package com.example.service;

import com.example.dto.LoginResponseDto;
import com.example.model.User;

public interface ITokenService {
    /**
     * Generates access and refresh tokens for a user.
     * @param user The user for whom to generate tokens.
     * @return A DTO containing the tokens.
     */
    LoginResponseDto generateAuthTokens(User user);
}
```
```java
// src/main/java/com/example/service/MessageService.java