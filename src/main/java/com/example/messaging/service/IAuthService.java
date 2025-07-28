package com.example.messaging.service;

import com.example.messaging.dto.AuthTokens;
import com.example.messaging.dto.InitiateLoginRequest;
import com.example.messaging.dto.VerifyLoginRequest;
import com.example.messaging.security.UserPrincipal;

import java.util.Optional;

public interface IAuthService {
    /**
     * Validates credentials and initiates the OTP sending process.
     * @param request DTO with phone number and password.
     */
    void initiateLogin(InitiateLoginRequest request);

    /**
     * Verifies the provided OTP and generates authentication tokens.
     * @param request DTO with phone number and OTP.
     * @return An AuthTokens object with access and refresh tokens.
     */
    AuthTokens verifyLogin(VerifyLoginRequest request);

    /**
     * Invalidates a user's refresh token, effectively logging them out.
     * @param refreshToken The refresh token to invalidate.
     */
    void logout(String refreshToken);

    /**
     * Validates an access token and returns the user principal if valid.
     * @param token The access token to validate.
     * @return An Optional containing the UserPrincipal if the token is valid.
     */
    Optional<UserPrincipal> validateAccessToken(String token);
}