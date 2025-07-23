package com.example.service;

public interface IOtpService {
    /**
     * Generates an OTP, stores it, and sends it to the user's phone.
     * @param phoneNumber The phone number to send the OTP to.
     */
    void generateAndSend(String phoneNumber);

    /**
     * Verifies if the provided OTP is valid for the given phone number.
     * @param phoneNumber The phone number.
     * @param otp The OTP to verify.
     * @return true if the OTP is valid, false otherwise.
     */
    boolean verify(String phoneNumber, String otp);
}