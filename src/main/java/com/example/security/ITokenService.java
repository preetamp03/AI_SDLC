package com.example.security;

import com.example.dto.LoginResponseDto;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface ITokenService {
    /**
     * Generates access and refresh tokens for a user.
     * @param userId The ID of the user.
     * @param phoneNumber The phone number of the user.
     * @return A DTO containing the access and refresh tokens.
     */
    LoginResponseDto generateAuthTokens(UUID userId, String phoneNumber);

    /**
     * Validates a JWT token.
     * @param token The JWT token string.
     * @return True if the token is valid, false otherwise.
     */
    boolean validateToken(String token);
    
    /**
     * Extracts the authentication object from a valid token.
     * @param token The JWT token string.
     * @return The Authentication object.
     */
    Authentication getAuthentication(String token);
}