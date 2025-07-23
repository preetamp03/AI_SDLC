package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "otps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Otp {
    @Id
    private String phoneNumber;

    @Column(nullable = false)
    private String code;
    
    @Column(nullable = false)
    private Instant expiresAt;
}
```
```java
// src/main/java/com/example/dto/InitiateLoginRequestDto.java