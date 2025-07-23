package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    private int statusCode;
    private String message;
    private String error;
}
```
```java
// src/main/java/com/example/dto/InitiateLoginRequestDto.java