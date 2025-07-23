package com.example.api.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for the user creation request payload.
 */
@Data
public class CreateUserRequest_User_2001 {

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    @Size(max = 254)
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 12, max = 128, message = "Password must be between 12 and 128 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,}$",
             message = "Password must be at least 12 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
    private String password;

    @NotBlank(message = "First name is required.")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters.")
    private String lastName;
}
```
```java
// src/main/java/com/example/api/user/model/UserResponse_User_2002.java