package com.example.service;

import com.example.dto.PaginatedChatsDto;
import com.example.exception.ForbiddenException;
import com.example.model.Chat;
import com.example.model.User;
import com.example.repository.IChatRepository;
import com.example.repository.IMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private IChatRepository chatRepository;
    @Mock
    private IMessageRepository messageRepository;
    
    @InjectMocks
    private ChatService chatService;

    /**
     * Test listing user chats successfully.
     */
    @Test
    void listUserChats_success() {
        UUID userId = UUID.randomUUID();
        Page<Chat> page = new PageImpl<>(Collections.emptyList());
        when(chatRepository.findChatsByUserId(any(UUID.class), any(), any(Pageable.class))).thenReturn(page);
        
        PaginatedChatsDto result = chatService.listUserChats(userId, 1, 10, "updatedAt", "desc", null);

        assertNotNull(result);
        assertTrue(result.getData().isEmpty());
    }

    /**
     * Test getting a chat for a user when access is forbidden.
     */
    @Test
    void getChatForUser_whenNotParticipant_throwsForbiddenException() {
        UUID userId = UUID.randomUUID();
        UUID chatId = UUID.randomUUID();
        when(chatRepository.findByIdAndParticipant(chatId, userId)).thenReturn(Optional.empty());

        assertThrows(ForbiddenException.class, () -> chatService.getChatForUser(chatId, userId));
    }
}
```
```java
// src/test/java/com/example/service/MessageServiceTest.java