package com.example.messaging.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TokenRepository {

    private final Map<String, String> validRefreshTokens = new ConcurrentHashMap<>();

    /**
     * Saves a refresh token, associating it with a user identifier.
     * @param token The refresh token.
     * @param userId The user ID.
     */
    public void save(String token, String userId) {
        validRefreshTokens.put(token, userId);
    }

    /**
     * Finds a refresh token to check if it's valid.
     * @param token The token to find.
     * @return An Optional containing the user ID if the token is valid.
     */
    public Optional<String> findUserIdByToken(String token) {
        return Optional.ofNullable(validRefreshTokens.get(token));
    }

    /**
     * Deletes (invalidates) a refresh token.
     * @param token The token to delete.
     */
    public void delete(String token) {
        validRefreshTokens.remove(token);
    }
}