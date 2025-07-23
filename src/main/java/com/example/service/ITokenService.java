package com.example.service;

import com.example.model.User;
import java.util.Map;

public interface ITokenService {
    /**
     * Generates a pair of authentication tokens (access and refresh) for a user.
     * @param user The user for whom to generate tokens.
     * @return A map containing the "accessToken" and "refreshToken".
     */
    Map<String, String> generateAuthTokens(User user);
}