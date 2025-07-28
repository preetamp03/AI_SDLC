package com.example.messaging.controller;

import com.example.messaging.dto.MessageDto;
import com.example.messaging.dto.SendMessageRequest;
import com.example.messaging.security.UserPrincipal;
import com.example.messaging.service.IMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserPrincipal userPrincipal;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userPrincipal = new UserPrincipal(userId, "user@example.com", "password");
    }

    /**
     * Tests successfully sending a message.
     */
    @Test
    @WithMockUser
    void sendMessage_shouldReturnCreatedMessage() throws Exception {
        UUID recipientId = UUID.randomUUID();
        SendMessageRequest request = new SendMessageRequest(recipientId, "Hello!");

        MessageDto responseDto = MessageDto.builder()
                .id(UUID.randomUUID())
                .senderId(userId)
                .content("Hello!")
                .createdAt(OffsetDateTime.now())
                .build();

        when(messageService.createMessage(eq(userId), any(SendMessageRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/messages")
                        .with(SecurityMockMvcRequestPostProcessors.user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Hello!"))
                .andExpect(jsonPath("$.senderId").value(userId.toString()));
    }
}