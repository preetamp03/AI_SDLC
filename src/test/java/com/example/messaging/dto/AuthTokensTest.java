package com.example.messaging.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AuthTokensTest {

    /**
     * Tests the constructor and getters of the AuthTokens DTO.
     */
    @Test
    void testAuthTokens() {
        String accessToken = "access.token";
        String refreshToken = "refresh.token";
        AuthTokens tokens = new AuthTokens(accessToken, refreshToken);

        assertThat(tokens.getAccessToken()).isEqualTo(accessToken);
        assertThat(tokens.getRefreshToken()).isEqualTo(refreshToken);

        tokens.setAccessToken("new.access.token");
        assertThat(tokens.getAccessToken()).isEqualTo("new.access.token");
    }
}