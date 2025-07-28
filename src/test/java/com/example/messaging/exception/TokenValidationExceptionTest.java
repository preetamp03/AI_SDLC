package com.example.messaging.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenValidationExceptionTest {

    /**
     * Tests that the exception can be thrown and its message is correct.
     */
    @Test
    void testException() {
        String message = "JWT token is expired";
        TokenValidationException exception = assertThrows(TokenValidationException.class, () -> {
            throw new TokenValidationException(message);
        });
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}