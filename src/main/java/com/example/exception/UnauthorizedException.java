package com.example.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
```
```java
// src/main/java/com/example/exception/ForbiddenException.java