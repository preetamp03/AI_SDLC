package com.example.service;

import com.example.dto.InitiateLoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.VerifyLoginRequestDto;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.UnauthorizedException;
import com.example.model.User;
import com.example.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final IOtpService otpService;
    private final ITokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Initiates login by validating credentials and sending an OTP.
     * @param request The login initiation request.
     */
    @Override
    @Transactional
    public void initiateLogin(InitiateLoginRequestDto request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new UnauthorizedException("Invalid phone number or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid phone number or password.");
        }

        otpService.generateAndSend(request.getPhoneNumber());
    }

    /**
     * Verifies the OTP and generates authentication tokens upon success.
     * @param request The login verification request.
     * @return A DTO containing the access and refresh tokens.
     */
    @Override
    @Transactional
    public LoginResponseDto verifyLogin(VerifyLoginRequestDto request) {
        boolean isOtpValid = otpService.verify(request.getPhoneNumber(), request.getOtp());
        if (!isOtpValid) {
            throw new UnauthorizedException("Invalid or expired OTP.");
        }

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for phone number."));

        return tokenService.generateAuthTokens(user);
    }
}
```
```java
// src/main/java/com/example/service/ChatService.java