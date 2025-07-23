package com.example.service;

import com.example.dto.PaginatedChatsDto;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Chat;
import com.example.model.ChatParticipant;
import com.example.model.User;
import com.example.repository.IChatRepository;
import com.example.repository.IMessageRepository;
import com.example.repository.IMessageReadStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock private IChatRepository chatRepository;
    @Mock private IMessageRepository messageRepository;
    @Mock private IMessageReadStatusRepository messageReadStatusRepository;
    @InjectMocks private ChatService chatService;

    @Test
    void listUserChats_shouldReturnPaginatedChats() {
        UUID currentUserId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        User currentUser = new User();
        currentUser.setId(currentUserId);
        User recipient = new User();
        recipient.setId(recipientId);
        recipient.setName("Recipient");

        Chat chat = new Chat();
        chat.setId(UUID.randomUUID());
        ChatParticipant p1 = new ChatParticipant(chat, currentUser);
        ChatParticipant p2 = new ChatParticipant(chat, recipient);
        chat.setParticipants(Set.of(p1, p2));
        
        Page<Chat> chatPage = new PageImpl<>(List.of(chat), pageable, 1);
        when(chatRepository.findChatsByUserId(currentUserId, pageable)).thenReturn(chatPage);
        when(messageRepository.countUnreadMessagesForUserInChat(chat.getId(), currentUserId)).thenReturn(0L);

        PaginatedChatsDto result = chatService.listUserChats(currentUserId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(1, result.getPagination().getTotalCount());
        assertEquals("Recipient", result.getData().get(0).getRecipient().getName());
    }

    @Test
    void getChatForUser_shouldReturnChat_whenFoundAndParticipant() {
        UUID chatId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Chat chat = new Chat();
        when(chatRepository.findByIdAndParticipant(chatId, userId)).thenReturn(Optional.of(chat));

        Chat result = chatService.getChatForUser(chatId, userId);

        assertNotNull(result);
    }

    @Test
    void getChatForUser_shouldThrowNotFound_whenNotFound() {
        UUID chatId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(chatRepository.findByIdAndParticipant(chatId, userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> chatService.getChatForUser(chatId, userId));
    }
}
```
```java
// src/test/java/com/example/service/MessageServiceTest.java