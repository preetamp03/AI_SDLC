package com.example.messaging.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceNotFoundExceptionTest {
    /**
     * Tests that the exception can be thrown and its message is correct.
     */
    @Test
    void testException() {
        String message = "Conversation not found";
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException(message);
        });
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}