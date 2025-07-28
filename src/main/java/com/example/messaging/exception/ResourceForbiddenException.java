package com.example.messaging.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResourceForbiddenException extends RuntimeException {
    /**
     * Exception for when a user tries to access a resource they don't own.
     * @param message The detail message.
     */
    public ResourceForbiddenException(String message) {
        super(message);
    }
}