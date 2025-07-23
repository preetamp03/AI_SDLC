package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    /**
     * Constructs a new UnauthorizedException with the specified detail message.
     * @param message the detail message.
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
```
```java
// src/main/java/com/example/logging/EventLogger.java