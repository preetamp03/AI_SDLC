package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    /**
     * Constructs a new ForbiddenException with the specified detail message.
     * @param message the detail message.
     */
    public ForbiddenException(String message) {
        super(message);
    }
}
```
```java
// src/main/java/com/example/exception/GlobalExceptionHandler.java