package com.example.messaging.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class OtpService implements IOtpService {

    private final Map<String, String> otpCache = new ConcurrentHashMap<>();
    private final Random random = new Random();

    /**
     * Generates a 6-digit OTP and logs it. In a real application, this would send an SMS.
     * @param key Typically the user's phone number.
     */
    @Override
    public void generateAndSendOtp(String key) {
        String otp = String.format("%06d", random.nextInt(999999));
        otpCache.put(key, otp);
        // In a real application, this would integrate with an SMS gateway.
        log.info("OTP for {}: {}", key, otp);
    }

    /**
     * Verifies if the provided OTP matches the one in the cache.
     * @param key The key used to store the OTP (phone number).
     * @param otp The user-submitted OTP.
     * @return True if the OTP is valid, false otherwise.
     */
    @Override
    public boolean verifyOtp(String key, String otp) {
        String cachedOtp = otpCache.get(key);
        if (cachedOtp != null && cachedOtp.equals(otp)) {
            otpCache.remove(key); // OTPs are single-use
            return true;
        }
        return false;
    }
}