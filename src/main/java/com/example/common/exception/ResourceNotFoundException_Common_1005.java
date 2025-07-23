package com.example.common.exception;

import com.example.common.model.ErrorCode_Common_1004;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource is not found.
 */
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException_Common_1005 extends RuntimeException {
    private final ErrorCode_Common_1004 errorCode;

    public ResourceNotFoundException_Common_1005(String message, ErrorCode_Common_1004 errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
```
```java
// src/main/java/com/example/common/config/SecurityConfig_Common_1006.java