package com.example.service;

import com.example.exception.ServiceUnavailableException;
import com.example.model.Otp;
import com.example.repository.IOtpRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OtpService implements IOtpService {

    private static final Logger log = LogManager.getLogger(OtpService.class);
    private final IOtpRepository otpRepository;
    private static final int OTP_EXPIRATION_MINUTES = 5;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void generateAndSend(String phoneNumber) {
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(100_000, 1_000_000));
        Instant expiresAt = Instant.now().plus(OTP_EXPIRATION_MINUTES, ChronoUnit.MINUTES);
        Otp otp = new Otp(phoneNumber, code, expiresAt);
        otpRepository.save(otp);

        // Simulate sending the OTP via an external service
        try {
            log.info("Simulating sending OTP {} to phone number {}", code, phoneNumber);
            // In a real application, you would integrate with an SMS gateway like Twilio.
            // if (smsGateway.send(phoneNumber, "Your OTP is: " + code) == FAILED) {
            //     throw new ServiceUnavailableException("Failed to send OTP.");
            // }
        } catch (Exception e) {
            log.error("Failed to send OTP for phone number {}: {}", phoneNumber, e.getMessage());
            throw new ServiceUnavailableException("Failed to send OTP. Please try again later.");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean verify(String phoneNumber, String otpCode) {
        return otpRepository.findValidOtp(phoneNumber, otpCode, Instant.now())
            .map(otp -> {
                otpRepository.delete(otp); // Consume the OTP
                return true;
            })
            .orElse(false);
    }
}
```
```java
// src/main/java/com/example/service/AuthService.java