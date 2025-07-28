package com.example.messaging.controller;

import com.example.messaging.dto.AuthTokens;
import com.example.messaging.dto.InitiateLoginRequest;
import com.example.messaging.dto.LogoutRequest;
import com.example.messaging.dto.VerifyLoginRequest;
import com.example.messaging.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    /**
     * Initiates the login process by validating credentials and sending an OTP.
     * @param request The request body containing phone number and password.
     * @return A 200 OK response on success.
     */
    @PostMapping("/login/initiate")
    public ResponseEntity<Void> initiateLogin(@Valid @RequestBody InitiateLoginRequest request) {
        authService.initiateLogin(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Verifies the OTP to finalize login and returns authentication tokens.
     * @param request The request body containing phone number and OTP.
     * @return A 200 OK response with authentication tokens.
     */
    @PostMapping("/login/verify")
    public ResponseEntity<AuthTokens> verifyLogin(@Valid @RequestBody VerifyLoginRequest request) {
        AuthTokens tokens = authService.verifyLogin(request);
        return ResponseEntity.ok(tokens);
    }

    /**
     * Logs the user out by invalidating their refresh token.
     * @param request The request body containing the refresh token.
     * @return A 204 No Content response on success.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}