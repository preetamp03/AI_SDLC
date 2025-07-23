package com.example.service;

import com.example.dto.InitiateLoginRequest;
import com.example.dto.LoginResponse;
import com.example.dto.VerifyLoginRequest;
import com.example.exception.UnauthorizedException;
import com.example.model.User;
import com.example.security.IPasswordHasher;
import com.example.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private IUserService userService;
    @Mock
    private IOtpService otpService;
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private IPasswordHasher passwordHasher;

    @InjectMocks
    private AuthService authService;

    private User user;
    private InitiateLoginRequest initiateLoginRequest;
    private VerifyLoginRequest verifyLoginRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .id(UUID.randomUUID())
            .phoneNumber("+1234567890")
            .passwordHash("hashedPassword")
            .build();
        initiateLoginRequest = new InitiateLoginRequest(user.getPhoneNumber(), "rawPassword");
        verifyLoginRequest = new VerifyLoginRequest();
        verifyLoginRequest.setPhoneNumber(user.getPhoneNumber());
        verifyLoginRequest.setOtp("123456");
    }

    @Test
    void initiateLogin_whenCredentialsAreValid_shouldSendOtp() {
        when(userService.findByPhoneNumber(user.getPhoneNumber())).thenReturn(user);
        when(passwordHasher.matches("rawPassword", "hashedPassword")).thenReturn(true);
        doNothing().when(otpService).generateAndSend(user.getPhoneNumber());

        authService.initiateLogin(initiateLoginRequest);

        verify(otpService).generateAndSend(user.getPhoneNumber());
    }

    @Test
    void initiateLogin_whenPasswordIsInvalid_shouldThrowUnauthorizedException() {
        when(userService.findByPhoneNumber(user.getPhoneNumber())).thenReturn(user);
        when(passwordHasher.matches("rawPassword", "hashedPassword")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.initiateLogin(initiateLoginRequest));
        verify(otpService, never()).generateAndSend(anyString());
    }

    @Test
    void verifyLogin_whenOtpIsValid_shouldReturnLoginResponse() {
        when(otpService.verify(user.getPhoneNumber(), "123456")).thenReturn(true);
        when(userService.findByPhoneNumber(user.getPhoneNumber())).thenReturn(user);
        when(tokenProvider.generateAuthTokens(user)).thenReturn(Map.of("accessToken", "access.token", "refreshToken", "refresh.token"));

        LoginResponse response = authService.verifyLogin(verifyLoginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access.token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh.token");
    }

    @Test
    void verifyLogin_whenOtpIsInvalid_shouldThrowUnauthorizedException() {
        when(otpService.verify(user.getPhoneNumber(), "123456")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.verifyLogin(verifyLoginRequest));
        verify(userService, never()).findByPhoneNumber(anyString());
        verify(tokenProvider, never()).generateAuthTokens(any(User.class));
    }
}