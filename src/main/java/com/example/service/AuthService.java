package com.example.service;

import com.example.dto.InitiateLoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.VerifyLoginRequestDto;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.UnauthorizedException;
import com.example.logging.LogExecutionTime;
import com.example.model.User;
import com.example.security.IPasswordHasher;
import com.example.security.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUserService userService;
    private final IOtpService otpService;
    private final ITokenService tokenService;
    private final IPasswordHasher passwordHasher;

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public void initiateLogin(InitiateLoginRequestDto requestDto) {
        User user = userService.findByPhoneNumber(requestDto.getPhoneNumber())
                .orElseThrow(() -> new UnauthorizedException("Invalid phone number or password."));

        if (!passwordHasher.check(requestDto.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid phone number or password.");
        }

        otpService.generateAndSend(user.getPhoneNumber());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    public LoginResponseDto verifyLogin(VerifyLoginRequestDto requestDto) {
        if (!otpService.verify(requestDto.getPhoneNumber(), requestDto.getOtp())) {
            // Check if an OTP was ever generated to give a more specific error
            if (userService.findByPhoneNumber(requestDto.getPhoneNumber()).isEmpty()) {
                 throw new ResourceNotFoundException("No pending login verification found for this phone number.");
            }
            throw new UnauthorizedException("Invalid or expired OTP.");
        }

        User user = userService.findByPhoneNumber(requestDto.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found after OTP verification. This should not happen."));
        
        return tokenService.generateAuthTokens(user.getId(), user.getPhoneNumber());
    }
}
```
```java
// src/main/java/com/example/service/ChatService.java