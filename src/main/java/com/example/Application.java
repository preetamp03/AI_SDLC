package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*
 * mermaid
 * sequenceDiagram
 *   participant Client
 *   participant AuthController
 *   participant AuthService
 *   participant UserService
 *   participant OtpService
 *   participant TokenService
 *
 *   Client->>AuthController: POST /auth/login/initiate (phoneNumber, password)
 *   AuthController->>AuthService: initiateLogin(dto)
 *   AuthService->>UserService: findByPhoneNumber(phone)
 *   UserService-->>AuthService: User
 *   AuthService->>AuthService: verifyPassword(password, user.hash)
 *   AuthService->>OtpService: generateAndSend(phone)
 *   OtpService-->>AuthService: void
 *   AuthService-->>AuthController: void
 *   AuthController-->>Client: 202 Accepted
 *
 *   Client->>AuthController: POST /auth/login/verify (phoneNumber, otp)
 *   AuthController->>AuthService: verifyLogin(dto)
 *   AuthService->>OtpService: verify(phone, otp)
 *   OtpService-->>AuthService: boolean (true)
 *   AuthService->>UserService: findByPhoneNumber(phone)
 *   UserService-->>AuthService: User
 *   AuthService->>TokenService: generateAuthTokens(user)
 *   TokenService-->>AuthService: LoginResponseDto
 *   AuthService-->>AuthController: LoginResponseDto
 *   AuthController-->>Client: 200 OK (Tokens)
 */
@SpringBootApplication
@EnableJpaAuditing
public class Application {
    /**
     * Main entry point for the Spring Boot application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
```java
// src/main/java/com/example/config/OpenApiConfig.java