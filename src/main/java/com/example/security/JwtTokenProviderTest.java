package com.example.security;

import com.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Set secret key and expiration using reflection
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "472D4B6150645367566B5970337336763979244226452948404D635165546857");
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenExpiration", 3600000L);
        
        testUser = User.builder()
                .id(UUID.randomUUID())
                .phoneNumber("+1234567890")
                .name("Test User")
                .build();
    }

    /**
     * Tests that a token can be generated and the username extracted correctly.
     */
    @Test
    void generateToken_shouldExtractCorrectUsername() {
        String token = jwtTokenProvider.generateAccessToken(testUser);
        assertNotNull(token);

        String extractedUsername = jwtTokenProvider.extractUsername(token);
        assertEquals(testUser.getPhoneNumber(), extractedUsername);
    }

    /**
     * Tests that a generated token is considered valid for the correct user.
     */
    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtTokenProvider.generateAccessToken(testUser);
        assertTrue(jwtTokenProvider.isTokenValid(token, testUser));
    }
}
```
```java
// src/test/java/com/example/service/AuthServiceTest.java