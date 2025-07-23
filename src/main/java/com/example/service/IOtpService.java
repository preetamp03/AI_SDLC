package com.example.service;

public interface IOtpService {
    /**
     * Generates an OTP, stores it, and sends it to the user's phone number.
     * @param phoneNumber The target phone number.
     */
    void generateAndSend(String phoneNumber);

    /**
     * Verifies if the provided OTP is valid for the phone number.
     * @param phoneNumber The phone number.
     * @param otp The OTP to verify.
     * @return True if valid, false otherwise.
     */
    boolean verify(String phoneNumber, String otp);
}
```
```java
// src/main/java/com/example/service/ITokenService.java