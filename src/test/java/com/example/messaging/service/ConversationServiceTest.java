package com.example.messaging.service;

import com.example.messaging.dto.PaginatedMessages;
import com.example.messaging.exception.ResourceForbiddenException;
import com.example.messaging.exception.ResourceNotFoundException;
import com.example.messaging.model.Conversation;
import com.example.messaging.model.Message;
import com.example.messaging.model.User;
import com.example.messaging.repository.ConversationRepository;
import com.example.messaging.repository.MessageRepository;
import com.example.messaging.util.EventLogger;
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
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {

    @Mock
    private ConversationRepository conversationRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private EventLogger eventLogger;

    @InjectMocks
    private ConversationService conversationService;

    /**
     * Tests fetching messages from a conversation successfully.
     */
    @Test
    void findMessagesByConversation_whenUserIsParticipant_shouldReturnMessages() {
        UUID userId = UUID.randomUUID();
        UUID conversationId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Conversation conversation = new Conversation();
        conversation.setParticipants(Set.of(user));
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(user);

        Page<Message> page = new PageImpl<>(Collections.singletonList(message));

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(messageRepository.findByConversationIdOrderByCreatedAtDesc(eq(conversationId), any(Pageable.class)))
                .thenReturn(page);

        PaginatedMessages result = conversationService.findMessagesByConversation(userId, conversationId, 1, 10);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getTotal()).isEqualTo(1);
    }

    /**
     * Tests fetching messages when the user is not a participant in the conversation.
     */
    @Test
    void findMessagesByConversation_whenUserNotParticipant_shouldThrowForbidden() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID conversationId = UUID.randomUUID();
        User otherUser = new User();
        otherUser.setId(otherUserId);
        Conversation conversation = new Conversation();
        conversation.setParticipants(Set.of(otherUser));

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));

        assertThrows(ResourceForbiddenException.class,
                () -> conversationService.findMessagesByConversation(userId, conversationId, 1, 10));
    }

    /**
     * Tests marking a conversation as read successfully.
     */
    @Test
    void markAsRead_whenUserIsParticipant_shouldSucceed() {
        UUID userId = UUID.randomUUID();
        UUID conversationId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Conversation conversation = new Conversation();
        conversation.setParticipants(Set.of(user));

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));

        conversationService.markAsRead(userId, conversationId);

        verify(messageRepository).markMessagesAsRead(conversationId, userId);
    }
    
    /**
     * Tests marking a non-existent conversation as read.
     */
    @Test
    void markAsRead_whenConversationNotFound_shouldThrowNotFound() {
        UUID userId = UUID.randomUUID();
        UUID conversationId = UUID.randomUUID();

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> conversationService.markAsRead(userId, conversationId));
    }
}