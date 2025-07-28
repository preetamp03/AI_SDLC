package com.example.messaging.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        // A 256-bit secret key encoded in Base64
        String secretKey = "vK3u/hLz4y7B+E(H+MbQeThWmZq4t7w!z%C*F-JaNdRfUjXn2r5u8x/A?D(G+KbP"; 
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenExpiration", 3600000L); // 1 hour
    }

    /**
     * Tests that a token can be generated and the user ID can be extracted from it.
     */
    @Test
    void generateTokenAndExtractUserId_shouldSucceed() {
        UUID userId = UUID.randomUUID();
        String token = jwtTokenProvider.generateAccessToken(userId);

        assertThat(token).isNotNull().isNotEmpty();
        
        UUID extractedUserId = jwtTokenProvider.extractUserId(token);
        assertThat(extractedUserId).isEqualTo(userId);
    }

    /**
     * Tests that a generated token is considered valid.
     */
    @Test
    void isTokenValid_withFreshToken_shouldReturnTrue() {
        UUID userId = UUID.randomUUID();
        String token = jwtTokenProvider.generateAccessToken(userId);

        boolean isValid = jwtTokenProvider.isTokenValid(token);
        assertThat(isValid).isTrue();
    }
}