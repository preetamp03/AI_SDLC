package com.example.controller;

import com.example.dto.PaginatedChatsDto;
import com.example.service.IChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IChatService chatService;

    // Mock beans required for security context in WebMvcTest
    @MockBean private com.example.security.JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean private com.example.security.UserDetailsServiceImpl userDetailsService;
    @MockBean private com.example.security.JwtTokenProvider jwtTokenProvider;

    private final UUID testUserId = UUID.randomUUID();

    @Test
    @WithMockUser(username = "c1a9b3d4-0e5f-4a7b-8c1d-2e3f4a5b6c7d") // Use a valid UUID string
    void listChats_shouldReturnPaginatedResult() throws Exception {
        PaginatedChatsDto dto = new PaginatedChatsDto(Collections.emptyList(), null);
        when(chatService.listUserChats(any(UUID.class), any(Pageable.class))).thenReturn(dto);

        mockMvc.perform(get("/chats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(username = "c1a9b3d4-0e5f-4a7b-8c1d-2e3f4a5b6c7d")
    void markChatAsRead_shouldReturnNoContent() throws Exception {
        UUID chatId = UUID.randomUUID();
        UUID userId = UUID.fromString("c1a9b3d4-0e5f-4a7b-8c1d-2e3f4a5b6c7d");
        
        mockMvc.perform(post("/chats/{chat_id}/read", chatId))
                .andExpect(status().isNoContent());

        // Verify that the service method was called with the correct parameters
        org.mockito.Mockito.verify(chatService).markChatAsRead(eq(userId), eq(chatId));
    }
}
```
```java
// src/test/java/com/example/controller/MessageControllerTest.java