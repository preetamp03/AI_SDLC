package com.example.messaging.controller;

import com.example.messaging.dto.AuthTokens;
import com.example.messaging.dto.InitiateLoginRequest;
import com.example.messaging.dto.LogoutRequest;
import com.example.messaging.dto.VerifyLoginRequest;
import com.example.messaging.service.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Tests successful login initiation.
     */
    @Test
    void initiateLogin_shouldReturnOk() throws Exception {
        InitiateLoginRequest request = new InitiateLoginRequest("1234567890", "password123");
        doNothing().when(authService).initiateLogin(any(InitiateLoginRequest.class));

        mockMvc.perform(post("/api/v1/auth/login/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    /**
     * Tests successful OTP verification and token generation.
     */
    @Test
    void verifyLogin_shouldReturnAuthTokens() throws Exception {
        VerifyLoginRequest request = new VerifyLoginRequest("1234567890", "123456");
        AuthTokens tokens = new AuthTokens("access_token", "refresh_token");
        when(authService.verifyLogin(any(VerifyLoginRequest.class))).thenReturn(tokens);

        mockMvc.perform(post("/api/v1/auth/login/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access_token"));
    }

    /**
     * Tests successful user logout.
     */
    @Test
    void logout_shouldReturnNoContent() throws Exception {
        LogoutRequest request = new LogoutRequest("refresh_token");
        doNothing().when(authService).logout(any(String.class));

        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}