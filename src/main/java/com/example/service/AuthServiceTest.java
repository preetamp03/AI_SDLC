package com.example.service;

import com.example.dto.InitiateLoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.VerifyLoginRequestDto;
import com.example.exception.UnauthorizedException;
import com.example.model.User;
import com.example.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private IUserRepository userRepository;
    @Mock
    private IOtpService otpService;
    @Mock
    private ITokenService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User user;
    private InitiateLoginRequestDto initiateLoginRequest;
    private VerifyLoginRequestDto verifyLoginRequest;

    @BeforeEach
    void setUp() {
        user = User.builder().phoneNumber("+123").password("hashedPassword").build();
        initiateLoginRequest = new InitiateLoginRequestDto();
        initiateLoginRequest.setPhoneNumber("+123");
        initiateLoginRequest.setPassword("password");

        verifyLoginRequest = new VerifyLoginRequestDto();
        verifyLoginRequest.setPhoneNumber("+123");
        verifyLoginRequest.setOtp("123456");
    }

    /**
     * Test successful login initiation.
     */
    @Test
    void initiateLogin_success() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        doNothing().when(otpService).generateAndSend(anyString());

        authService.initiateLogin(initiateLoginRequest);

        verify(otpService, times(1)).generateAndSend("+123");
    }

    /**
     * Test login initiation with an invalid password.
     */
    @Test
    void initiateLogin_invalidPassword_throwsUnauthorized() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.initiateLogin(initiateLoginRequest));
    }

    /**
     * Test successful OTP verification.
     */
    @Test
    void verifyLogin_success() {
        when(otpService.verify(anyString(), anyString())).thenReturn(true);
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(tokenService.generateAuthTokens(any(User.class)))
                .thenReturn(new LoginResponseDto("access", "refresh"));

        LoginResponseDto result = authService.verifyLogin(verifyLoginRequest);

        assertNotNull(result);
        assertEquals("access", result.getAccessToken());
    }

    /**
     * Test OTP verification with an invalid OTP.
     */
    @Test
    void verifyLogin_invalidOtp_throwsUnauthorized() {
        when(otpService.verify(anyString(), anyString())).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> authService.verifyLogin(verifyLoginRequest));
    }
}
```
```java
// src/test/java/com/example/service/ChatServiceTest.java