package com.example.controller;

import com.example.dto.InitiateLoginRequest;
import com.example.dto.LoginResponse;
import com.example.dto.VerifyLoginRequest;
import com.example.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    /**
     * Initiates the login process by sending an OTP.
     * @param request The request body containing phone number and password.
     * @return A response entity indicating success.
     */
    @PostMapping("/login/initiate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Map<String, String>> initiateLogin(@Valid @RequestBody InitiateLoginRequest request) {
        authService.initiateLogin(request);
        return ResponseEntity.accepted().body(Map.of("message", "An OTP has been sent to your phone number."));
    }

    /**
     * Verifies the OTP and returns authentication tokens upon success.
     * @param request The request body containing phone number and OTP.
     * @return A response entity with access and refresh tokens.
     */
    @PostMapping("/login/verify")
    public ResponseEntity<LoginResponse> verifyLogin(@Valid @RequestBody VerifyLoginRequest request) {
        LoginResponse tokens = authService.verifyLogin(request);
        return ResponseEntity.ok(tokens);
    }
}