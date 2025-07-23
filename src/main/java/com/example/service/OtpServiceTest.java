package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.Otp;
import com.example.repository.IOtpRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private IOtpRepository otpRepository;

    @InjectMocks
    private OtpService otpService;

    /**
     * Test successful OTP verification.
     */
    @Test
    void verify_validOtp_returnsTrueAndDeletesOtp() {
        String phone = "+123";
        String code = "123456";
        Otp otp = new Otp(phone, code, Instant.now().plusSeconds(300));

        when(otpRepository.findByPhoneNumberAndCodeAndExpiresAtAfter(eq(phone), eq(code), any(Instant.class)))
                .thenReturn(Optional.of(otp));

        boolean result = otpService.verify(phone, code);

        assertTrue(result);
        verify(otpRepository, times(1)).delete(otp);
    }

    /**
     * Test OTP verification failure.
     */
    @Test
    void verify_invalidOtp_returnsFalse() {
        String phone = "+123";
        String code = "123456";

        when(otpRepository.findByPhoneNumberAndCodeAndExpiresAtAfter(anyString(), anyString(), any(Instant.class)))
                .thenReturn(Optional.empty());
        // Mock the check to prevent ResourceNotFoundException for this specific test case
        when(otpRepository.findById(phone)).thenReturn(Optional.of(new Otp()));

        boolean result = otpService.verify(phone, code);

        assertFalse(result);
        verify(otpRepository, never()).delete(any());
    }
    
    /**
     * Test OTP verification when no OTP was ever initiated.
     */
    @Test
    void verify_noOtpInitiated_throwsResourceNotFoundException() {
        String phone = "+123";
        String code = "123456";
        when(otpRepository.findByPhoneNumberAndCodeAndExpiresAtAfter(anyString(), anyString(), any(Instant.class)))
            .thenReturn(Optional.empty());
        when(otpRepository.findById(phone)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> otpService.verify(phone, code));
    }

    /**
     * Test OTP generation and sending simulation.
     */
    @Test
    void generateAndSend_savesOtp() {
        ReflectionTestUtils.setField(otpService, "otpExpirationMinutes", 5);
        when(otpRepository.save(any(Otp.class))).thenAnswer(i -> i.getArguments()[0]);

        otpService.generateAndSend("+123");

        verify(otpRepository, times(1)).save(any(Otp.class));
    }
}
```
```java
// src/test/java/com/example/service/TokenServiceTest.java