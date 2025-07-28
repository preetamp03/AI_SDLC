package com.example.messaging.service;

import com.example.messaging.dto.AuthTokens;
import com.example.messaging.dto.InitiateLoginRequest;
import com.example.messaging.dto.VerifyLoginRequest;
import com.example.messaging.exception.InvalidCredentialsException;
import com.example.messaging.model.User;
import com.example.messaging.repository.UserRepository;
import com.example.messaging.security.UserPrincipal;
import com.example.messaging.util.EventLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private IPasswordService passwordService;
    @Mock
    private IOtpService otpService;
    @Mock
    private ITokenService tokenService;
    @Mock
    private EventLogger eventLogger;

    @InjectMocks
    private AuthService authService;

    /**
     * Tests successful login initiation.
     */
    @Test
    void initiateLogin_withValidCredentials_shouldSucceed() {
        InitiateLoginRequest request = new InitiateLoginRequest("1234567890", "password");
        User user = new User();
        user.setPassword("hashedPassword");
        
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(user));
        when(passwordService.comparePassword(request.getPassword(), user.getPassword())).thenReturn(true);

        authService.initiateLogin(request);
        
        verify(otpService).generateAndSendOtp(request.getPhoneNumber());
    }
    
    /**
     * Tests login initiation with an invalid password.
     */
    @Test
    void initiateLogin_withInvalidPassword_shouldThrowException() {
        InitiateLoginRequest request = new InitiateLoginRequest("1234567890", "wrongPassword");
        User user = new User();
        user.setPassword("hashedPassword");

        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(user));
        when(passwordService.comparePassword(request.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.initiateLogin(request));
        verify(otpService, never()).generateAndSendOtp(anyString());
    }

    /**
     * Tests successful OTP verification.
     */
    @Test
    void verifyLogin_withValidOtp_shouldReturnTokens() {
        VerifyLoginRequest request = new VerifyLoginRequest("1234567890", "123456");
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword("hashedPassword");
        AuthTokens expectedTokens = new AuthTokens("access", "refresh");
        
        when(otpService.verifyOtp(request.getPhoneNumber(), request.getOtp())).thenReturn(true);
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(user));
        when(tokenService.generateAuthTokens(any(UserPrincipal.class))).thenReturn(expectedTokens);

        AuthTokens actualTokens = authService.verifyLogin(request);
        
        assertThat(actualTokens).isEqualTo(expectedTokens);
    }
    
    /**
     * Tests OTP verification with an invalid OTP.
     */
    @Test
    void verifyLogin_withInvalidOtp_shouldThrowException() {
        VerifyLoginRequest request = new VerifyLoginRequest("1234567890", "wrongOtp");
        
        when(otpService.verifyOtp(request.getPhoneNumber(), request.getOtp())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.verifyLogin(request));
        verify(userRepository, never()).findByPhoneNumber(anyString());
    }
    
    /**
     * Tests logout functionality.
     */
    @Test
    void logout_shouldCallTokenService() {
        String refreshToken = "some-refresh-token";
        authService.logout(refreshToken);
        verify(tokenService).invalidateRefreshToken(refreshToken);
    }
}