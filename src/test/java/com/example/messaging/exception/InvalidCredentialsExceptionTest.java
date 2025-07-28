package com.example.messaging.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvalidCredentialsExceptionTest {

    /**
     * Tests that the exception can be thrown and its message is correct.
     */
    @Test
    void testException() {
        String message = "Invalid password";
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            throw new InvalidCredentialsException(message);
        });
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}