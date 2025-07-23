package com.example.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
```
```java
// src/main/java/com/example/exception/ServiceUnavailableException.java