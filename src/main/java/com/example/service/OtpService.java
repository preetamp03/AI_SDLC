package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.Otp;
import com.example.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class OtpService implements IOtpService {

    private final OtpRepository otpRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a 6-digit OTP, saves it, and simulates sending it.
     * @param phoneNumber The phone number to associate with the OTP.
     */
    @Override
    public void generateAndSend(String phoneNumber) {
        String code = String.format("%06d", secureRandom.nextInt(999999));
        Instant expiresAt = Instant.now().plus(5, ChronoUnit.MINUTES);

        Otp otp = new Otp(phoneNumber, code, expiresAt);
        otpRepository.save(otp);

        // In a real app, this would use an SMS gateway service like Twilio
        System.out.println("---- OTP Service ----");
        System.out.println("Sending OTP " + code + " to " + phoneNumber);
        System.out.println("---------------------");
    }

    /**
     * Verifies an OTP for a given phone number. The OTP is consumed upon successful verification.
     * @param phoneNumber The phone number.
     * @param otpCode The OTP to verify.
     * @return true if the OTP is valid and not expired, false otherwise.
     */
    @Override
    public boolean verify(String phoneNumber, String otpCode) {
        Otp otp = otpRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new ResourceNotFoundException("No pending login verification found for this phone number."));

        if (otp.getExpiresAt().isBefore(Instant.now())) {
            otpRepository.delete(otp);
            return false; // Expired
        }

        if (otp.getCode().equals(otpCode)) {
            otpRepository.delete(otp); // Consume OTP
            return true;
        }

        return false;
    }
}