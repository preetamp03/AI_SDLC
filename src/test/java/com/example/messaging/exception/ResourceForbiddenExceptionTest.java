package com.example.messaging.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceForbiddenExceptionTest {

    /**
     * Tests that the exception can be thrown and its message is correct.
     */
    @Test
    void testException() {
        String message = "Access to this conversation is denied";
        ResourceForbiddenException exception = assertThrows(ResourceForbiddenException.class, () -> {
            throw new ResourceForbiddenException(message);
        });
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}