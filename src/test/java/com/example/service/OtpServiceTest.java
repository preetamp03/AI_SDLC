package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.Otp;
import com.example.repository.OtpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private OtpRepository otpRepository;

    @InjectMocks
    private OtpService otpService;

    private String phoneNumber;

    @BeforeEach
    void setUp() {
        phoneNumber = "+1987654321";
    }

    @Test
    void generateAndSend_shouldSaveOtp() {
        otpService.generateAndSend(phoneNumber);
        verify(otpRepository).save(any(Otp.class));
    }

    @Test
    void verify_withValidOtp_shouldReturnTrueAndDeletesOtp() {
        Instant future = Instant.now().plus(5, ChronoUnit.MINUTES);
        Otp otp = new Otp(phoneNumber, "123456", future);
        when(otpRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(otp));

        boolean result = otpService.verify(phoneNumber, "123456");

        assertThat(result).isTrue();
        verify(otpRepository).delete(otp);
    }

    @Test
    void verify_withInvalidOtp_shouldReturnFalse() {
        Instant future = Instant.now().plus(5, ChronoUnit.MINUTES);
        Otp otp = new Otp(phoneNumber, "123456", future);
        when(otpRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(otp));

        boolean result = otpService.verify(phoneNumber, "654321");

        assertThat(result).isFalse();
        verify(otpRepository, never()).delete(any());
    }

    @Test
    void verify_withExpiredOtp_shouldReturnFalseAndDeletesOtp() {
        Instant past = Instant.now().minus(5, ChronoUnit.MINUTES);
        Otp otp = new Otp(phoneNumber, "123456", past);
        when(otpRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(otp));

        boolean result = otpService.verify(phoneNumber, "123456");

        assertThat(result).isFalse();
        verify(otpRepository).delete(otp);
    }

    @Test
    void verify_withNoPendingOtp_shouldThrowResourceNotFoundException() {
        when(otpRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> otpService.verify(phoneNumber, "123456"));
    }
}