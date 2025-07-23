package com.example.controller;

import com.example.dto.InitiateLoginRequest;
import com.example.dto.LoginResponse;
import com.example.dto.VerifyLoginRequest;
import com.example.service.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for this test
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAuthService authService;

    @Test
    void initiateLogin_shouldReturnAccepted() throws Exception {
        InitiateLoginRequest request = new InitiateLoginRequest("+15551234567", "password");
        doNothing().when(authService).initiateLogin(any(InitiateLoginRequest.class));

        mockMvc.perform(post("/auth/login/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.message").value("An OTP has been sent to your phone number."));
    }

    @Test
    void verifyLogin_shouldReturnOkWithTokens() throws Exception {
        VerifyLoginRequest request = new VerifyLoginRequest();
        request.setPhoneNumber("+15551234567");
        request.setOtp("123456");

        LoginResponse response = LoginResponse.builder()
            .accessToken("access.token")
            .refreshToken("refresh.token")
            .build();

        when(authService.verifyLogin(any(VerifyLoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("access.token"))
            .andExpect(jsonPath("$.refreshToken").value("refresh.token"));
    }
}