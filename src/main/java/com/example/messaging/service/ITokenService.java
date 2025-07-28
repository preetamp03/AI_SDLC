package com.example.messaging.service;

import com.example.messaging.dto.AuthTokens;
import com.example.messaging.security.UserPrincipal;
import java.util.Optional;

public interface ITokenService {
    /**
     * Generates both access and refresh tokens for a user.
     * @param userPrincipal The user principal containing user details.
     * @return An AuthTokens DTO with the new tokens.
     */
    AuthTokens generateAuthTokens(UserPrincipal userPrincipal);

    /**
     * Invalidates a given refresh token.
     * @param token The refresh token to invalidate.
     */
    void invalidateRefreshToken(String token);

    /**
     * Validates an access token and returns the user principal if valid.
     * @param token The access token to validate.
     * @return An Optional of UserPrincipal if the token is valid.
     */
    Optional<UserPrincipal> validateAccessToken(String token);
}