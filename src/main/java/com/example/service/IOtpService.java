package com.example.service;

public interface IOtpService {
    /**
     * Generates a 6-digit OTP, stores it, and sends it (simulation).
     * @param phoneNumber The phone number to send the OTP to.
     */
    void generateAndSend(String phoneNumber);

    /**
     * Verifies if the provided OTP is valid for the given phone number.
     * Consumes the OTP on successful verification.
     * @param phoneNumber The phone number.
     * @param otp The OTP code.
     * @return true if the OTP is valid, false otherwise.
     */
    boolean verify(String phoneNumber, String otp);
}
```
```java
// src/main/java/com/example/service/IAuthService.java