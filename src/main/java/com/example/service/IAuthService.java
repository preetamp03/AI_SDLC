package com.example.service;

import com.example.dto.InitiateLoginRequest;
import com.example.dto.LoginResponse;
import com.example.dto.VerifyLoginRequest;

public interface IAuthService {
    /**
     * Handles the first step of login, validating credentials and sending OTP.
     * @param request DTO with phone number and password.
     */
    void initiateLogin(InitiateLoginRequest request);

    /**
     * Handles the second step of login, verifying OTP and issuing tokens.
     * @param request DTO with phone number and OTP.
     * @return A DTO containing access and refresh tokens.
     */
    LoginResponse verifyLogin(VerifyLoginRequest request);
}