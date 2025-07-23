package com.example.api.user.model;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO for the public user information returned by the API.
 */
@Data
@Builder
public class UserResponse_User_2002 {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Instant createdAt;
}
```
```java
// src/main/java/com/example/api/user/model/UserEntity_User_2003.java