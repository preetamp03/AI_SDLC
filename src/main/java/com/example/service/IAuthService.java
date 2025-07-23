package com.example.service;

import com.example.dto.InitiateLoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.VerifyLoginRequestDto;

public interface IAuthService {
    /**
     * Initiates the login process by validating credentials and sending an OTP.
     * @param requestDto The DTO containing the phone number and password.
     */
    void initiateLogin(InitiateLoginRequestDto requestDto);

    /**
     * Verifies the OTP and returns authentication tokens upon success.
     * @param requestDto The DTO containing the phone number and OTP.
     * @return A DTO with access and refresh tokens.
     */
    LoginResponseDto verifyLogin(VerifyLoginRequestDto requestDto);
}
```
```java
// src/main/java/com/example/service/IChatService.java