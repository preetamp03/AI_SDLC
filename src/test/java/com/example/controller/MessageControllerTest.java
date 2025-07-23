package com.example.controller;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.PaginationDto;
import com.example.dto.SendMessageRequest;
import com.example.model.User;
import com.example.service.IMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
@AutoConfigureMockMvc(addFilters = false)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IMessageService messageService;

    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = User.builder().id(testUserId).phoneNumber("+12222222222").build();
    }

    @Test
    @WithMockUser
    void listChatMessages_shouldReturnPaginatedMessages() throws Exception {
        UUID chatId = UUID.randomUUID();
        PaginationDto pagination = PaginationDto.builder().currentPage(1).totalPages(1).build();
        MessageDto messageDto = MessageDto.builder().id(UUID.randomUUID()).content("Hello").build();
        PaginatedMessagesDto responseDto = new PaginatedMessagesDto(Collections.singletonList(messageDto), pagination);

        when(messageService.listChatMessages(eq(testUserId), eq(chatId), any(Pageable.class))).thenReturn(responseDto);

        mockMvc.perform(get("/chats/{chat_id}/messages", chatId).with(user(testUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].content").value("Hello"))
            .andExpect(jsonPath("$.pagination.currentPage").value(1));
    }

    @Test
    @WithMockUser
    void sendMessage_shouldReturnCreatedMessage() throws Exception {
        SendMessageRequest request = new SendMessageRequest();
        request.setRecipientId(UUID.randomUUID());
        request.setContent("New message");

        MessageDto responseDto = MessageDto.builder()
            .id(UUID.randomUUID())
            .content("New message")
            .senderId(testUserId)
            .build();

        when(messageService.sendMessage(eq(testUserId), any(SendMessageRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/messages")
                .with(user(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("New message"))
            .andExpect(jsonPath("$.senderId").value(testUserId.toString()));
    }
}