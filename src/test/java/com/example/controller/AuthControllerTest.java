package com.example.controller;

import com.example.dto.InitiateLoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.VerifyLoginRequestDto;
import com.example.service.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAuthService authService;

    // These are mocked because WebMvcTest doesn't load the full security context
    @MockBean private com.example.security.JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean private com.example.security.UserDetailsServiceImpl userDetailsService;
    @MockBean private com.example.security.JwtTokenProvider jwtTokenProvider;


    @Test
    void initiateLogin_shouldReturnAccepted() throws Exception {
        InitiateLoginRequestDto requestDto = new InitiateLoginRequestDto();
        requestDto.setPhoneNumber("+15551234567");
        requestDto.setPassword("password123");
        
        doNothing().when(authService).initiateLogin(requestDto);
        
        mockMvc.perform(post("/auth/login/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("An OTP has been sent to your phone number."));
    }

    @Test
    void verifyLogin_shouldReturnTokens() throws Exception {
        VerifyLoginRequestDto requestDto = new VerifyLoginRequestDto();
        requestDto.setPhoneNumber("+15551234567");
        requestDto.setOtp("123456");

        LoginResponseDto responseDto = new LoginResponseDto("access.token", "refresh.token");
        when(authService.verifyLogin(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/auth/login/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access.token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh.token"));
    }
}