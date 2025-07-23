package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyLoginRequestDto {
    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format.")
    private String phoneNumber;

    @NotBlank(message = "OTP is required.")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits.")
    private String otp;
}
```
```java
// src/main/java/com/example/exception/BadRequestException.java