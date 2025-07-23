package com.example.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic tests for custom exception classes.
 */
class ExceptionTest {

    @Test
    void testBadRequestException() {
        Exception exception = assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException("test message");
        });
        assertEquals("test message", exception.getMessage());
    }

    @Test
    void testForbiddenException() {
        Exception exception = assertThrows(ForbiddenException.class, () -> {
            throw new ForbiddenException("test message");
        });
        assertEquals("test message", exception.getMessage());
    }

    @Test
    void testResourceNotFoundException() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("test message");
        });
        assertEquals("test message", exception.getMessage());
    }

    @Test
    void testUnauthorizedException() {
        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            throw new UnauthorizedException("test message");
        });
        assertEquals("test message", exception.getMessage());
    }
}
```
```java
// src/test/java/com/example/logging/LoggingTest.java