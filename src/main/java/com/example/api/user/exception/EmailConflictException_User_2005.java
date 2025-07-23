package com.example.api.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when attempting to create a user with an email that already exists.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EmailConflictException_User_2005 extends RuntimeException {
    public EmailConflictException_User_2005(String message) {
        super(message);
    }
}
```
```java
// src/main/java/com/example/api/user/service/UserService_User_2006.java