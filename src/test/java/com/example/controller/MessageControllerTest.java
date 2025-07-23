package com.example.controller;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.SendMessageRequestDto;
import com.example.service.IMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IMessageService messageService;

    // Mock beans required for security context in WebMvcTest
    @MockBean private com.example.security.JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean private com.example.security.UserDetailsServiceImpl userDetailsService;
    @MockBean private com.example.security.JwtTokenProvider jwtTokenProvider;
    
    private final UUID testUserId = UUID.fromString("c1a9b3d4-0e5f-4a7b-8c1d-2e3f4a5b6c7d");

    @Test
    @WithMockUser(username = "c1a9b3d4-0e5f-4a7b-8c1d-2e3f4a5b6c7d")
    void sendMessage_shouldReturnCreatedMessage() throws Exception {
        UUID recipientId = UUID.randomUUID();
        SendMessageRequestDto requestDto = new SendMessageRequestDto();
        requestDto.setRecipientId(recipientId);
        requestDto.setContent("Hello World");
        
        MessageDto responseDto = MessageDto.builder()
                .id(UUID.randomUUID())
                .chatId(UUID.randomUUID())
                .senderId(testUserId)
                .content("Hello World")
                .timestamp(Instant.now())
                .build();
        
        when(messageService.sendMessage(eq(testUserId), any(SendMessageRequestDto.class))).thenReturn(responseDto);
        
        mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Hello World"))
                .andExpect(jsonPath("$.senderId").value(testUserId.toString()));
    }
    
    @Test
    @WithMockUser(username = "c1a9b3d4-0e5f-4a7b-8c1d-2e3f4a5b6c7d")
    void listChatMessages_shouldReturnPaginatedMessages() throws Exception {
        UUID chatId = UUID.randomUUID();
        PaginatedMessagesDto responseDto = new PaginatedMessagesDto(Collections.emptyList(), null);

        when(messageService.listChatMessages(eq(testUserId), eq(chatId), any(Pageable.class))).thenReturn(responseDto);

        mockMvc.perform(get("/chats/{chat_id}/messages", chatId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }
}