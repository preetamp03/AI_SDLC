package com.example.repository;

import com.example.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface IOtpRepository extends JpaRepository<Otp, String> {
    /**
     * Finds an OTP by phone number, code, and ensures it has not expired.
     * @param phoneNumber The user's phone number.
     * @param code The OTP code.
     * @param now The current time to check against expiry.
     * @return An Optional containing the OTP if valid.
     */
    Optional<Otp> findByPhoneNumberAndCodeAndExpiresAtAfter(String phoneNumber, String code, Instant now);
}
```
```java
// src/main/java/com/example/repository/IUserRepository.java