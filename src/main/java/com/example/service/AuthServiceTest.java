package com.example.service;

import com.example.dto.InitiateLoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.VerifyLoginRequestDto;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.UnauthorizedException;
import com.example.model.User;
import com.example.security.IPasswordHasher;
import com.example.security.ITokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private IUserService userService;
    @Mock private IOtpService otpService;
    @Mock private ITokenService tokenService;
    @Mock private IPasswordHasher passwordHasher;
    @InjectMocks private AuthService authService;

    @Test
    void initiateLogin_shouldSucceed() {
        InitiateLoginRequestDto dto = new InitiateLoginRequestDto();
        dto.setPhoneNumber("+123");
        dto.setPassword("pass");

        User user = new User();
        user.setPhoneNumber("+123");
        user.setPasswordHash("hashedPass");
        when(userService.findByPhoneNumber("+123")).thenReturn(Optional.of(user));
        when(passwordHasher.check("pass", "hashedPass")).thenReturn(true);

        authService.initiateLogin(dto);

        verify(otpService).generateAndSend("+123");
    }

    @Test
    void initiateLogin_shouldFailForInvalidPassword() {
        InitiateLoginRequestDto dto = new InitiateLoginRequestDto();
        dto.setPhoneNumber("+123");
        dto.setPassword("wrongPass");

        User user = new User();
        user.setPasswordHash("hashedPass");
        when(userService.findByPhoneNumber("+123")).thenReturn(Optional.of(user));
        when(passwordHasher.check("wrongPass", "hashedPass")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.initiateLogin(dto));
        verify(otpService, never()).generateAndSend(anyString());
    }

    @Test
    void initiateLogin_shouldFailForUnknownUser() {
        InitiateLoginRequestDto dto = new InitiateLoginRequestDto();
        dto.setPhoneNumber("+123");
        when(userService.findByPhoneNumber("+123")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.initiateLogin(dto));
    }
    
    @Test
    void verifyLogin_shouldSucceed() {
        VerifyLoginRequestDto dto = new VerifyLoginRequestDto();
        dto.setPhoneNumber("+123");
        dto.setOtp("123456");
        
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setPhoneNumber("+123");
        
        LoginResponseDto tokens = new LoginResponseDto("access", "refresh");
        
        when(otpService.verify("+123", "123456")).thenReturn(true);
        when(userService.findByPhoneNumber("+123")).thenReturn(Optional.of(user));
        when(tokenService.generateAuthTokens(user.getId(), user.getPhoneNumber())).thenReturn(tokens);
        
        LoginResponseDto result = authService.verifyLogin(dto);
        
        assertNotNull(result);
        assertEquals("access", result.getAccessToken());
    }
    
    @Test
    void verifyLogin_shouldFailForInvalidOtp() {
        VerifyLoginRequestDto dto = new VerifyLoginRequestDto();
        dto.setPhoneNumber("+123");
        dto.setOtp("wrongOtp");
        
        when(otpService.verify("+123", "wrongOtp")).thenReturn(false);
        // We still need a user to exist for the correct error message
        when(userService.findByPhoneNumber("+123")).thenReturn(Optional.of(new User()));
        
        assertThrows(UnauthorizedException.class, () -> authService.verifyLogin(dto));
    }

    @Test
    void verifyLogin_shouldFailForNoPendingOtp() {
        VerifyLoginRequestDto dto = new VerifyLoginRequestDto();
        dto.setPhoneNumber("+123");
        dto.setOtp("someOtp");
        
        when(otpService.verify("+123", "someOtp")).thenReturn(false);
        when(userService.findByPhoneNumber("+123")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> authService.verifyLogin(dto));
    }
}
```
```java
// src/test/java/com/example/service/ChatServiceTest.java