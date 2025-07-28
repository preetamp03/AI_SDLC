package com.example.messaging.service;

import com.example.messaging.dto.AuthTokens;
import com.example.messaging.dto.InitiateLoginRequest;
import com.example.messaging.dto.VerifyLoginRequest;
import com.example.messaging.exception.InvalidCredentialsException;
import com.example.messaging.exception.ResourceNotFoundException;
import com.example.messaging.model.User;
import com.example.messaging.repository.UserRepository;
import com.example.messaging.security.UserPrincipal;
import com.example.messaging.util.EventLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final IPasswordService passwordService;
    private final IOtpService otpService;
    private final ITokenService tokenService;
    private final EventLogger eventLogger;

    /**
     * Validates credentials and initiates the OTP sending process.
     * @param request DTO with phone number and password.
     */
    @Override
    public void initiateLogin(InitiateLoginRequest request) {
        long startTime = System.currentTimeMillis();
        eventLogger.logEvent("LoginInitiation_Start", "phoneNumber=" + request.getPhoneNumber());

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordService.comparePassword(request.getPassword(), user.getPassword())) {
            eventLogger.logEvent("LoginInitiation_Failure_InvalidPassword", "userId=" + user.getId());
            throw new InvalidCredentialsException("Invalid credentials");
        }

        otpService.generateAndSendOtp(user.getPhoneNumber());
        
        eventLogger.logEvent("LoginInitiation_Success", "phoneNumber=" + request.getPhoneNumber(), System.currentTimeMillis() - startTime);
    }

    /**
     * Verifies the provided OTP and generates authentication tokens.
     * @param request DTO with phone number and OTP.
     * @return An AuthTokens object with access and refresh tokens.
     */
    @Override
    public AuthTokens verifyLogin(VerifyLoginRequest request) {
        long startTime = System.currentTimeMillis();
        eventLogger.logEvent("LoginVerification_Start", "phoneNumber=" + request.getPhoneNumber());

        if (!otpService.verifyOtp(request.getPhoneNumber(), request.getOtp())) {
            eventLogger.logEvent("LoginVerification_Failure_InvalidOtp", "phoneNumber=" + request.getPhoneNumber());
            throw new InvalidCredentialsException("Invalid or expired OTP");
        }

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found after OTP verification"));

        AuthTokens tokens = tokenService.generateAuthTokens(new UserPrincipal(user.getId(), user.getPhoneNumber(), user.getPassword()));

        eventLogger.logEvent("LoginVerification_Success", "userId=" + user.getId(), System.currentTimeMillis() - startTime);
        return tokens;
    }

    /**
     * Invalidates a user's refresh token, effectively logging them out.
     * @param refreshToken The refresh token to invalidate.
     */
    @Override
    public void logout(String refreshToken) {
        long startTime = System.currentTimeMillis();
        eventLogger.logEvent("Logout_Start", null);
        tokenService.invalidateRefreshToken(refreshToken);
        eventLogger.logEvent("Logout_Success", null, System.currentTimeMillis() - startTime);
    }
    
    /**
     * Validates an access token and returns the user principal if valid.
     * @param token The access token to validate.
     * @return An Optional containing the UserPrincipal if the token is valid.
     */
    @Override
    public Optional<UserPrincipal> validateAccessToken(String token) {
        return tokenService.validateAccessToken(token);
    }
}