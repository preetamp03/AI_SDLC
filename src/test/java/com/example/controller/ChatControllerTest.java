package com.example.controller;

import com.example.dto.ChatSummaryDto;
import com.example.dto.PaginatedChatsDto;
import com.example.dto.PaginationDto;
import com.example.model.User;
import com.example.service.IChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IChatService chatService;

    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = User.builder().id(testUserId).phoneNumber("+11111111111").build();
    }

    @Test
    @WithMockUser
    void listChats_shouldReturnPaginatedChats() throws Exception {
        PaginationDto pagination = PaginationDto.builder().currentPage(1).totalPages(1).totalCount(1).build();
        ChatSummaryDto chatSummary = ChatSummaryDto.builder().id(UUID.randomUUID()).build();
        PaginatedChatsDto responseDto = new PaginatedChatsDto(Collections.singletonList(chatSummary), pagination);

        when(chatService.listUserChats(eq(testUserId), any(Pageable.class), eq(null))).thenReturn(responseDto);

        mockMvc.perform(get("/chats").with(user(testUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].id").value(chatSummary.getId().toString()))
            .andExpect(jsonPath("$.pagination.currentPage").value(1));
    }

    @Test
    @WithMockUser
    void markChatAsRead_shouldReturnNoContent() throws Exception {
        UUID chatId = UUID.randomUUID();
        doNothing().when(chatService).markChatAsRead(testUserId, chatId);

        mockMvc.perform(post("/chats/{chat_id}/read", chatId)
                .with(user(testUser)))
            .andExpect(status().isNoContent());
    }
}