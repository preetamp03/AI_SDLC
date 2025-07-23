package com.example.controller;

import com.example.dto.PaginatedChatsDto;
import com.example.model.User;
import com.example.service.IChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
// Note: In a real app with full security config, you'd need more setup
// This is simplified by not loading the full security context.
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IChatService chatService;
    
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(UUID.randomUUID()).phoneNumber("+15555555555").build();
    }

    /**
     * Test for GET /chats endpoint.
     */
    @Test
    @WithMockUser // Provides a mock SecurityContext
    void listChats_shouldReturnPaginatedChats() throws Exception {
        when(chatService.listUserChats(any(UUID.class), anyInt(), anyInt(), anyString(), anyString(), any()))
                .thenReturn(new PaginatedChatsDto(Collections.emptyList(), null));

        mockMvc.perform(get("/api/v1/chats").with(user(testUser)))
                .andExpect(status().isOk());

        verify(chatService).listUserChats(eq(testUser.getId()), eq(1), eq(20), eq("lastMessageTime"), eq("desc"), eq(null));
    }

    /**
     * Test for POST /chats/{chat_id}/read endpoint.
     */
    @Test
    @WithMockUser
    void markChatAsRead_shouldReturnNoContent() throws Exception {
        UUID chatId = UUID.randomUUID();
        doNothing().when(chatService).markChatAsRead(any(UUID.class), any(UUID.class));

        mockMvc.perform(post("/api/v1/chats/{chat_id}/read", chatId).with(user(testUser)))
                .andExpect(status().isNoContent());

        verify(chatService).markChatAsRead(eq(testUser.getId()), eq(chatId));
    }
}
```
```java
// src/test/java/com/example/controller/MessageControllerTest.java