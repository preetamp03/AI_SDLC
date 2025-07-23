package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    /**
     * Constructs a new BadRequestException with the specified detail message.
     * @param message the detail message.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
```
```java
// src/main/java/com/example/exception/ForbiddenException.java