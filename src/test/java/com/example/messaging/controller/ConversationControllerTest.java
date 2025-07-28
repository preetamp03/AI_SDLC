package com.example.messaging.controller;

import com.example.messaging.dto.PaginatedConversations;
import com.example.messaging.dto.PaginatedMessages;
import com.example.messaging.security.UserPrincipal;
import com.example.messaging.service.IConversationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConversationController.class)
class ConversationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IConversationService conversationService;

    private UserPrincipal userPrincipal;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userPrincipal = new UserPrincipal(userId, "user@example.com", "password");
    }

    /**
     * Tests fetching a list of conversations.
     */
    @Test
    @WithMockUser
    void listConversations_shouldReturnPaginatedConversations() throws Exception {
        PaginatedConversations response = new PaginatedConversations(Collections.emptyList(), 1, 20, 0);
        when(conversationService.findUserConversations(eq(userId), anyInt(), anyInt(), anyString(), any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/conversations")
                        .with(SecurityMockMvcRequestPostProcessors.user(userPrincipal)))
                .andExpect(status().isOk());
    }

    /**
     * Tests fetching a list of messages from a conversation.
     */
    @Test
    @WithMockUser
    void listMessages_shouldReturnPaginatedMessages() throws Exception {
        UUID conversationId = UUID.randomUUID();
        PaginatedMessages response = new PaginatedMessages(Collections.emptyList(), 1, 50, 0);
        when(conversationService.findMessagesByConversation(eq(userId), eq(conversationId), anyInt(), anyInt()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/conversations/{id}/messages", conversationId)
                        .with(SecurityMockMvcRequestPostProcessors.user(userPrincipal)))
                .andExpect(status().isOk());
    }

    /**
     * Tests marking a conversation as read.
     */
    @Test
    @WithMockUser
    void markConversationAsRead_shouldReturnNoContent() throws Exception {
        UUID conversationId = UUID.randomUUID();
        doNothing().when(conversationService).markAsRead(userId, conversationId);

        mockMvc.perform(post("/api/v1/conversations/{id}/read", conversationId)
                        .with(SecurityMockMvcRequestPostProcessors.user(userPrincipal)))
                .andExpect(status().isNoContent());
    }
}