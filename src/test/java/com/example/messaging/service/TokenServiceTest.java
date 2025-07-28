package com.example.messaging.service;

import com.example.messaging.dto.AuthTokens;
import com.example.messaging.model.User;
import com.example.messaging.repository.TokenRepository;
import com.example.messaging.repository.UserRepository;
import com.example.messaging.security.JwtTokenProvider;
import com.example.messaging.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TokenService tokenService;

    /**
     * Tests the generation of authentication tokens.
     */
    @Test
    void generateAuthTokens_shouldReturnTokensAndSaveRefreshToken() {
        UUID userId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "phone", "pass");
        String accessToken = "access.token";
        String refreshToken = "refresh.token";

        when(jwtTokenProvider.generateAccessToken(userId)).thenReturn(accessToken);
        when(jwtTokenProvider.generateRefreshToken(userId)).thenReturn(refreshToken);

        AuthTokens tokens = tokenService.generateAuthTokens(principal);

        assertThat(tokens.getAccessToken()).isEqualTo(accessToken);
        assertThat(tokens.getRefreshToken()).isEqualTo(refreshToken);
        verify(tokenRepository).save(refreshToken, userId.toString());
    }

    /**
     * Tests the validation of a valid access token.
     */
    @Test
    void validateAccessToken_withValidToken_shouldReturnUserPrincipal() {
        String token = "valid.token";
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setPhoneNumber("phone");
        user.setPassword("pass");

        when(jwtTokenProvider.isTokenValid(token)).thenReturn(true);
        when(jwtTokenProvider.extractUserId(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<UserPrincipal> principalOpt = tokenService.validateAccessToken(token);

        assertThat(principalOpt).isPresent();
        assertThat(principalOpt.get().getId()).isEqualTo(userId);
    }

    /**
     * Tests the validation of an invalid access token.
     */
    @Test
    void validateAccessToken_withInvalidToken_shouldReturnEmpty() {
        String token = "invalid.token";
        when(jwtTokenProvider.isTokenValid(token)).thenReturn(false);

        Optional<UserPrincipal> principalOpt = tokenService.validateAccessToken(token);

        assertThat(principalOpt).isNotPresent();
        verify(userRepository, never()).findById(any());
    }
}