package com.example.service;

import com.example.dto.LoginResponseDto;
import com.example.model.User;
import com.example.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Generates both access and refresh tokens for a user.
     * @param user The user object.
     * @return A DTO containing the new tokens.
     */
    @Override
    public LoginResponseDto generateAuthTokens(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
```
```java
// src/main/resources/application.properties
server.port=8080

# Database Configuration (H2 In-Memory)
spring.datasource.url=jdbc:h2:mem:chatdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

# Springdoc OpenAPI configuration
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Log4j2 Configuration
logging.config=classpath:log4j2.xml

# JWT Configuration
# Generate a secure secret, e.g., using: openssl rand -base64 32
application.security.jwt.secret-key=GENERATE_A_SECURE_32_BYTE_BASE64_SECRET_KEY_HERE
application.security.jwt.access-token-expiration-ms=3600000 # 1 hour
application.security.jwt.refresh-token-expiration-ms=604800000 # 7 days

# OTP Configuration
application.otp.expiration-minutes=5

# Kafka (Placeholder, not enabled)
# spring.kafka.bootstrap-servers=localhost:9092
```
```java
// src/main/resources/log4j2.xml