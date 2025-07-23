package com.example.service;

import com.example.model.Otp;
import com.example.repository.IOtpRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private IOtpRepository otpRepository;

    @InjectMocks
    private OtpService otpService;

    @Test
    void generateAndSend_shouldSaveOtp() {
        String phoneNumber = "+1234567890";
        ArgumentCaptor<Otp> otpCaptor = ArgumentCaptor.forClass(Otp.class);

        otpService.generateAndSend(phoneNumber);

        verify(otpRepository).save(otpCaptor.capture());
        Otp savedOtp = otpCaptor.getValue();

        assertEquals(phoneNumber, savedOtp.getPhoneNumber());
        assertNotNull(savedOtp.getCode());
        assertEquals(6, savedOtp.getCode().length());
        assertTrue(savedOtp.getExpiresAt().isAfter(Instant.now()));
    }

    @Test
    void verify_shouldReturnTrueAndDeletesOtp_whenValid() {
        String phoneNumber = "+1234567890";
        String code = "123456";
        Otp otp = new Otp(phoneNumber, code, Instant.now().plusSeconds(300));

        when(otpRepository.findValidOtp(eq(phoneNumber), eq(code), any(Instant.class))).thenReturn(Optional.of(otp));

        boolean result = otpService.verify(phoneNumber, code);

        assertTrue(result);
        verify(otpRepository).delete(otp);
    }

    @Test
    void verify_shouldReturnFalse_whenInvalid() {
        String phoneNumber = "+1234567890";
        String code = "123456";

        when(otpRepository.findValidOtp(eq(phoneNumber), eq(code), any(Instant.class))).thenReturn(Optional.empty());

        boolean result = otpService.verify(phoneNumber, code);

        assertFalse(result);
    }
}
```
```java
// src/test/java/com/example/service/AuthServiceTest.java