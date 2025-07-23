package com.example.controller;

import com.example.dto.InitiateLoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.VerifyLoginRequestDto;
import com.example.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    /**
     * Initiates the phone login process by sending an OTP.
     * @param requestDto The request body containing phone number and password.
     * @return A 202 Accepted response.
     */
    @PostMapping("/login/initiate")
    public ResponseEntity<Map<String, String>> initiateLogin(@Valid @RequestBody InitiateLoginRequestDto requestDto) {
        authService.initiateLogin(requestDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("message", "An OTP has been sent to your phone number."));
    }

    /**
     * Verifies the OTP and completes the login, returning auth tokens.
     * @param requestDto The request body containing phone number and OTP.
     * @return A 200 OK response with access and refresh tokens.
     */
    @PostMapping("/login/verify")
    public ResponseEntity<LoginResponseDto> verifyLogin(@Valid @RequestBody VerifyLoginRequestDto requestDto) {
        LoginResponseDto tokens = authService.verifyLogin(requestDto);
        return ResponseEntity.ok(tokens);
    }
}
```
```java
// src/main/java/com/example/controller/ChatController.java