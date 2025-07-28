package com.example.messaging.service;

public interface IOtpService {
    /**
     * Generates a 6-digit OTP, stores it with the given key, and sends it.
     * @param key Typically the user's phone number.
     */
    void generateAndSendOtp(String key);

    /**
     * Verifies if the provided OTP matches the stored one for the given key.
     * @param key The key used to store the OTP.
     * @param otp The user-submitted OTP.
     * @return True if the OTP is valid, false otherwise.
     */
    boolean verifyOtp(String key, String otp);
}