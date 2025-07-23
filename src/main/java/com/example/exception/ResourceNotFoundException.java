package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * @param message the detail message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```
```java
// src/main/java/com/example/exception/UnauthorizedException.java