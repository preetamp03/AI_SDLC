package com.example.controller;

import com.example.dto.InitiateLoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.VerifyLoginRequestDto;
import com.example.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LogManager.getLogger(AuthController.class);
    private final IAuthService authService;

    /**
     * Initiates the login process by sending an OTP.
     * @param request DTO containing phone number and password.
     * @return A 202 Accepted response.
     */
    @PostMapping("/login/initiate")
    public ResponseEntity<Map<String, String>> initiateLogin(@Valid @RequestBody InitiateLoginRequestDto request) {
        log.info("Initiating login for phone number: {}", request.getPhoneNumber());
        authService.initiateLogin(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("message", "An OTP has been sent to your phone number."));
    }

    /**
     * Verifies the OTP and completes the login, returning auth tokens.
     * @param request DTO containing phone number and OTP.
     * @return A 200 OK response with access and refresh tokens.
     */
    @PostMapping("/login/verify")
    public ResponseEntity<LoginResponseDto> verifyLogin(@Valid @RequestBody VerifyLoginRequestDto request) {
        log.info("Verifying OTP for phone number: {}", request.getPhoneNumber());
        LoginResponseDto response = authService.verifyLogin(request);
        return ResponseEntity.ok(response);
    }
}
```
```java
// src/main/java/com/example/controller/ChatController.java