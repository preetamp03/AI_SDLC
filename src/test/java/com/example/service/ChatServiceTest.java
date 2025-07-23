package com.example.service;

import com.example.dto.PaginatedChatsDto;
import com.example.exception.ForbiddenException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Chat;
import com.example.model.User;
import com.example.repository.ChatParticipantRepository;
import com.example.repository.ChatRepository;
import com.example.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;
    @Mock
    private ChatParticipantRepository chatParticipantRepository;
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ChatService chatService;

    private UUID userId;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void listUserChats_shouldReturnPaginatedChats() {
        User user = User.builder().id(userId).build();
        Chat chat = new Chat();
        chat.setId(UUID.randomUUID());
        Page<Chat> chatPage = new PageImpl<>(Collections.singletonList(chat));

        when(chatRepository.findChatsByUserId(eq(userId), eq(null), any(Pageable.class))).thenReturn(chatPage);

        PaginatedChatsDto result = chatService.listUserChats(userId, pageable, null);

        assertThat(result).isNotNull();
        assertThat(result.getData()).hasSize(1);
        assertThat(result.getPagination().getTotalCount()).isEqualTo(1);
    }

    @Test
    void getChatForUser_whenUserIsParticipant_shouldReturnChat() {
        UUID chatId = UUID.randomUUID();
        Chat chat = new Chat();
        chat.setId(chatId);

        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));
        when(chatParticipantRepository.existsByChatIdAndUserId(chatId, userId)).thenReturn(true);

        Chat result = chatService.getChatForUser(chatId, userId);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(chatId);
    }

    @Test
    void getChatForUser_whenChatNotFound_shouldThrowResourceNotFoundException() {
        UUID chatId = UUID.randomUUID();
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> chatService.getChatForUser(chatId, userId));
    }

    @Test
    void getChatForUser_whenUserNotParticipant_shouldThrowForbiddenException() {
        UUID chatId = UUID.randomUUID();
        Chat chat = new Chat();
        chat.setId(chatId);

        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));
        when(chatParticipantRepository.existsByChatIdAndUserId(chatId, userId)).thenReturn(false);

        assertThrows(ForbiddenException.class, () -> chatService.getChatForUser(chatId, userId));
    }

    @Test
    void markChatAsRead_whenUserIsParticipant_shouldComplete() {
        UUID chatId = UUID.randomUUID();
        Chat chat = new Chat();
        chat.setId(chatId);

        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));
        when(chatParticipantRepository.existsByChatIdAndUserId(chatId, userId)).thenReturn(true);

        assertDoesNotThrow(() -> chatService.markChatAsRead(userId, chatId));
    }
}