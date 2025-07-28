package com.example.messaging.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

class TokenRepositoryTest {

    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        tokenRepository = new TokenRepository();
    }

    /**
     * Tests saving and finding a token.
     */
    @Test
    void saveAndFindToken_shouldSucceed() {
        String token = "my-refresh-token";
        String userId = "user-123";
        tokenRepository.save(token, userId);

        Optional<String> foundUserId = tokenRepository.findUserIdByToken(token);

        assertThat(foundUserId).isPresent().contains(userId);
    }

    /**
     * Tests that a non-existent token is not found.
     */
    @Test
    void findNonExistentToken_shouldReturnEmpty() {
        Optional<String> foundUserId = tokenRepository.findUserIdByToken("non-existent-token");
        assertThat(foundUserId).isNotPresent();
    }

    /**
     * Tests deleting a token.
     */
    @Test
    void deleteToken_shouldRemoveToken() {
        String token = "my-refresh-token";
        String userId = "user-123";
        tokenRepository.save(token, userId);
        
        tokenRepository.delete(token);

        Optional<String> foundUserId = tokenRepository.findUserIdByToken(token);
        assertThat(foundUserId).isNotPresent();
    }
}