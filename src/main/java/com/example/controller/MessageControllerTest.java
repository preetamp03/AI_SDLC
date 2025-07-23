package com.example.controller;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.SendMessageRequestDto;
import com.example.model.User;
import com.example.service.IMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(UUID.randomUUID()).phoneNumber("+15555555555").build();
    }

    /**
     * Test for POST /messages endpoint.
     */
    @Test
    @WithMockUser
    void sendMessage_shouldReturnCreatedMessage() throws Exception {
        SendMessageRequestDto request = new SendMessageRequestDto();
        request.setRecipientId(UUID.randomUUID());
        request.setContent("Hello!");

        MessageDto responseDto = MessageDto.builder()
                .id(UUID.randomUUID())
                .content("Hello!")
                .senderId(testUser.getId())
                .timestamp(Instant.now())
                .build();

        when(messageService.sendMessage(any(UUID.class), any(SendMessageRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/messages")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Hello!"));
    }

    /**
     * Test for GET /chats/{chat_id}/messages endpoint.
     */
    @Test
    @WithMockUser
    void listChatMessages_shouldReturnPaginatedMessages() throws Exception {
        UUID chatId = UUID.randomUUID();
        PaginatedMessagesDto response = new PaginatedMessagesDto(Collections.emptyList(), null);

        when(messageService.listChatMessages(any(UUID.class), any(UUID.class), anyInt(), anyInt())).thenReturn(response);

        mockMvc.perform(get("/api/v1/chats/{chat_id}/messages", chatId)
                        .with(user(testUser))
                        .param("page", "1")
                        .param("limit", "50"))
                .andExpect(status().isOk());
    }
}
```
```java
// src/test/java/com/example/dto/DtoTest.java