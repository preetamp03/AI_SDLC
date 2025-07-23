package com.example.service;

import com.example.dto.LoginResponseDto;
import com.example.model.User;
import com.example.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private TokenService tokenService;

    /**
     * Test that token generation returns a DTO with both tokens.
     */
    @Test
    void generateAuthTokens_shouldReturnDtoWithTokens() {
        User user = User.builder().id(UUID.randomUUID()).build();
        when(jwtTokenProvider.generateAccessToken(any(User.class))).thenReturn("access.token");
        when(jwtTokenProvider.generateRefreshToken(any(User.class))).thenReturn("refresh.token");

        LoginResponseDto response = tokenService.generateAuthTokens(user);

        assertNotNull(response);
        assertEquals("access.token", response.getAccessToken());
        assertEquals("refresh.token", response.getRefreshToken());
    }
}
```