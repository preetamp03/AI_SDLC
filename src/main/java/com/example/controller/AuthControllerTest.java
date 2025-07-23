package com.example.controller;

import com.example.dto.InitiateLoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.VerifyLoginRequestDto;
import com.example.service.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {AuthController.class})
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for this test
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test for POST /auth/login/initiate endpoint.
     */
    @Test
    void initiateLogin_shouldReturnAccepted() throws Exception {
        InitiateLoginRequestDto request = new InitiateLoginRequestDto();
        request.setPhoneNumber("+14155552671");
        request.setPassword("password123");

        doNothing().when(authService).initiateLogin(any(InitiateLoginRequestDto.class));

        mockMvc.perform(post("/api/v1/auth/login/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("An OTP has been sent to your phone number."));
    }

    /**
     * Test for POST /auth/login/verify endpoint.
     */
    @Test
    void verifyLogin_shouldReturnTokens() throws Exception {
        VerifyLoginRequestDto request = new VerifyLoginRequestDto();
        request.setPhoneNumber("+14155552671");
        request.setOtp("123456");

        LoginResponseDto responseDto = new LoginResponseDto("access.token", "refresh.token");
        when(authService.verifyLogin(any(VerifyLoginRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/auth/login/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access.token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh.token"));
    }
}
```
```java
// src/test/java/com/example/controller/ChatControllerTest.java