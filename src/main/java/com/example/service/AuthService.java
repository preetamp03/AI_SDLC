package com.example.service;

import com.example.dto.InitiateLoginRequest;
import com.example.dto.LoginResponse;
import com.example.dto.VerifyLoginRequest;
import com.example.exception.UnauthorizedException;
import com.example.model.User;
import com.example.security.IPasswordHasher;
import com.example.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUserService userService;
    private final IOtpService otpService;
    private final JwtTokenProvider tokenProvider;
    private final IPasswordHasher passwordHasher;

    /**
     * Initiates the login process by validating credentials and sending an OTP.
     * @param request The login initiation request.
     */
    @Override
    @Transactional
    public void initiateLogin(InitiateLoginRequest request) {
        User user = userService.findByPhoneNumber(request.getPhoneNumber());
        if (!passwordHasher.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid phone number or password.");
        }
        otpService.generateAndSend(request.getPhoneNumber());
    }

    /**
     * Verifies the provided OTP and generates authentication tokens if valid.
     * @param request The login verification request.
     * @return A DTO containing the access and refresh tokens.
     */
    @Override
    @Transactional
    public LoginResponse verifyLogin(VerifyLoginRequest request) {
        boolean isOtpValid = otpService.verify(request.getPhoneNumber(), request.getOtp());
        if (!isOtpValid) {
            throw new UnauthorizedException("Invalid or expired OTP.");
        }
        User user = userService.findByPhoneNumber(request.getPhoneNumber());
        var tokens = tokenProvider.generateAuthTokens(user);

        return LoginResponse.builder()
            .accessToken(tokens.get("accessToken"))
            .refreshToken(tokens.get("refreshToken"))
            .build();
    }
}