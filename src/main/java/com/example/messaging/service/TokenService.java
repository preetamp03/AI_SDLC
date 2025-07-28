package com.example.messaging.service;

import com.example.messaging.dto.AuthTokens;
import com.example.messaging.exception.ResourceNotFoundException;
import com.example.messaging.model.User;
import com.example.messaging.repository.TokenRepository;
import com.example.messaging.repository.UserRepository;
import com.example.messaging.security.JwtTokenProvider;
import com.example.messaging.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    /**
     * Generates both access and refresh tokens for a user.
     * @param userPrincipal The user principal containing user details.
     * @return An AuthTokens DTO with the new tokens.
     */
    @Override
    public AuthTokens generateAuthTokens(UserPrincipal userPrincipal) {
        UUID userId = userPrincipal.getId();
        String accessToken = jwtTokenProvider.generateAccessToken(userId);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);
        tokenRepository.save(refreshToken, userId.toString());
        return new AuthTokens(accessToken, refreshToken);
    }

    /**
     * Invalidates a given refresh token.
     * @param token The refresh token to invalidate.
     */
    @Override
    public void invalidateRefreshToken(String token) {
        tokenRepository.delete(token);
    }

    /**
     * Validates an access token and returns the user principal if valid.
     * @param token The access token to validate.
     * @return An Optional of UserPrincipal if the token is valid.
     */
    @Override
    public Optional<UserPrincipal> validateAccessToken(String token) {
        if (!jwtTokenProvider.isTokenValid(token)) {
            return Optional.empty();
        }
        UUID userId = jwtTokenProvider.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User from token not found"));
        
        UserPrincipal userPrincipal = new UserPrincipal(user.getId(), user.getPhoneNumber(), user.getPassword());
        return Optional.of(userPrincipal);
    }
}