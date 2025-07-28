package com.example.messaging.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenValidationException extends RuntimeException {
    /**
     * Exception for invalid, expired, or malformed JWT tokens.
     * @param message The detail message.
     */
    public TokenValidationException(String message) {
        super(message);
    }
}