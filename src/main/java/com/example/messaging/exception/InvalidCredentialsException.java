package com.example.messaging.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends RuntimeException {
    /**
     * Exception for invalid login credentials or OTP.
     * @param message The detail message.
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}