package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.Otp;
import com.example.repository.IOtpRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpService implements IOtpService {

    private static final Logger log = LogManager.getLogger(OtpService.class);
    private static final SecureRandom random = new SecureRandom();

    private final IOtpRepository otpRepository;

    @Value("${application.otp.expiration-minutes}")
    private int otpExpirationMinutes;

    /**
     * Generates a 6-digit OTP, saves it, and simulates sending it.
     * @param phoneNumber The target phone number.
     */
    @Override
    @Transactional
    public void generateAndSend(String phoneNumber) {
        String code = String.format("%06d", random.nextInt(999999));
        Instant expiresAt = Instant.now().plus(otpExpirationMinutes, ChronoUnit.MINUTES);

        Otp otp = new Otp(phoneNumber, code, expiresAt);
        otpRepository.save(otp);

        // In a real application, you would integrate with an SMS gateway like Twilio.
        log.info("Simulating sending OTP {} to phone number {}", code, phoneNumber);
    }

    /**
     * Verifies the provided OTP. If valid, the OTP is consumed (deleted).
     * @param phoneNumber The phone number.
     * @param code The OTP to verify.
     * @return True if valid, false otherwise.
     */
    @Override
    @Transactional
    public boolean verify(String phoneNumber, String code) {
        Optional<Otp> otpOptional = otpRepository.findByPhoneNumberAndCodeAndExpiresAtAfter(phoneNumber, code, Instant.now());

        if (otpOptional.isEmpty()) {
            // To prevent enumeration attacks, check if an OTP was ever generated
            if (otpRepository.findById(phoneNumber).isEmpty()) {
                 throw new ResourceNotFoundException("No pending login verification found for this phone number.");
            }
            return false;
        }

        // Consume the OTP after successful verification
        otpRepository.delete(otpOptional.get());
        return true;
    }
}
```
```java
// src/main/java/com/example/service/TokenService.java