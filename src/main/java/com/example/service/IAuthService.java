package com.example.service;

import com.example.dto.InitiateLoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.VerifyLoginRequestDto;

public interface IAuthService {
    /**
     * Initiates the login process for a user.
     * @param initiateLoginDto DTO with login credentials.
     */
    void initiateLogin(InitiateLoginRequestDto initiateLoginDto);

    /**
     * Verifies a login attempt using an OTP.
     * @param verifyLoginDto DTO with phone number and OTP.
     * @return DTO with access and refresh tokens.
     */
    LoginResponseDto verifyLogin(VerifyLoginRequestDto verifyLoginDto);
}
```
```java
// src/main/java/com/example/service/IChatService.java