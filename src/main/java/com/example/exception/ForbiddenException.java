package com.example.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
```
```java
// src/main/java/com/example/exception/BadRequestException.java