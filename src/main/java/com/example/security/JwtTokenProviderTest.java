package com.example.security;

import com.example.dto.LoginResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @Mock
    private UserDetailsService userDetailsService;

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "test-secret-key-that-is-long-enough-for-hs512-testing");
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenExpirationMs", 3600000L);
        ReflectionTestUtils.setField(jwtTokenProvider, "refreshTokenExpirationMs", 86400000L);
        jwtTokenProvider.init();
    }

    @Test
    void generateAuthTokens_shouldCreateTokens() {
        UUID userId = UUID.randomUUID();
        String phoneNumber = "+1234567890";
        
        LoginResponseDto tokens = jwtTokenProvider.generateAuthTokens(userId, phoneNumber);

        assertNotNull(tokens);
        assertNotNull(tokens.getAccessToken());
        assertNotNull(tokens.getRefreshToken());
        assertTrue(jwtTokenProvider.validateToken(tokens.getAccessToken()));
    }

    @Test
    void getUserIdFromToken_shouldReturnCorrectId() {
        UUID userId = UUID.randomUUID();
        LoginResponseDto tokens = jwtTokenProvider.generateAuthTokens(userId, "+1234567890");

        UUID extractedId = jwtTokenProvider.getUserIdFromToken(tokens.getAccessToken());
        assertEquals(userId, extractedId);
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidToken() {
        assertFalse(jwtTokenProvider.validateToken("invalid.token.string"));
    }
    
    @Test
    void getAuthentication_shouldReturnAuthenticationObject() {
        UUID userId = UUID.randomUUID();
        String phoneNumber = "+1234567890";
        LoginResponseDto tokens = jwtTokenProvider.generateAuthTokens(userId, phoneNumber);
        
        UserDetails userDetails = new User(userId.toString(), "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        var authentication = jwtTokenProvider.getAuthentication(tokens.getAccessToken());

        assertNotNull(authentication);
        assertEquals(userId.toString(), authentication.getName());
    }
}
```
```java
// src/test/java/com/example/security/UserDetailsServiceImplTest.java