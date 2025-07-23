package com.example.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends ApiException {
    public ServiceUnavailableException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
```
```java
// src/main/java/com/example/exception/GlobalExceptionHandler.java