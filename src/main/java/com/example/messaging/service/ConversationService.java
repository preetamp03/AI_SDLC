package com.example.messaging.service;

import com.example.messaging.dto.*;
import com.example.messaging.exception.ResourceForbiddenException;
import com.example.messaging.exception.ResourceNotFoundException;
import com.example.messaging.model.Conversation;
import com.example.messaging.model.Message;
import com.example.messaging.model.User;
import com.example.messaging.repository.ConversationRepository;
import com.example.messaging.repository.MessageRepository;
import com.example.messaging.repository.UserRepository;
import com.example.messaging.util.EventLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService implements IConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final EventLogger eventLogger;

    /**
     * Finds all conversations for a specific user with pagination, sorting, and search.
     * @param userId The user's ID.
     * @param page The page number.
     * @param limit The number of items per page.
     * @param sortBy The sorting criteria.
     * @param query The search query.
     * @return A paginated response of Conversation DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedConversations findUserConversations(UUID userId, int page, int limit, String sortBy, String query) {
        long startTime = System.currentTimeMillis();
        eventLogger.logEvent("FindConversations_Start", "userId=" + userId);
        
        // 'seen' sort is not directly supported, defaulting to 'updatedAt'
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        
        Page<Conversation> conversationPage = conversationRepository.findByUserIdWithSearch(userId, query, pageable);

        List<ConversationDto> dtos = conversationPage.getContent().stream()
                .map(conv -> convertToDto(conv, userId))
                .collect(Collectors.toList());

        eventLogger.logEvent("FindConversations_Success", "userId=" + userId + ", count=" + dtos.size(), System.currentTimeMillis() - startTime);
        return new PaginatedConversations(dtos, page, limit, conversationPage.getTotalElements());
    }

    /**
     * Finds all messages within a specific conversation with pagination.
     * @param userId The ID of the user making the request.
     * @param conversationId The conversation's ID.
     * @param page The page number.
     * @param limit The number of items per page.
     * @return A paginated response of Message DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedMessages findMessagesByConversation(UUID userId, UUID conversationId, int page, int limit) {
        long startTime = System.currentTimeMillis();
        eventLogger.logEvent("FindMessages_Start", "userId=" + userId + ", conversationId=" + conversationId);
        
        verifyUserAccessToConversation(userId, conversationId);
        
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Message> messagePage = messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable);

        List<MessageDto> dtos = messagePage.getContent().stream()
                .map(this::convertMessageToDto)
                .collect(Collectors.toList());
        
        eventLogger.logEvent("FindMessages_Success", "conversationId=" + conversationId + ", count=" + dtos.size(), System.currentTimeMillis() - startTime);
        return new PaginatedMessages(dtos, page, limit, messagePage.getTotalElements());
    }

    /**
     * Marks all messages in a conversation as read by the user.
     * @param userId The ID of the user making the request.
     * @param conversationId The conversation's ID.
     */
    @Override
    @Transactional
    public void markAsRead(UUID userId, UUID conversationId) {
        long startTime = System.currentTimeMillis();
        eventLogger.logEvent("MarkAsRead_Start", "userId=" + userId + ", conversationId=" + conversationId);
        
        verifyUserAccessToConversation(userId, conversationId);
        messageRepository.markMessagesAsRead(conversationId, userId);
        
        eventLogger.logEvent("MarkAsRead_Success", "conversationId=" + conversationId, System.currentTimeMillis() - startTime);
    }

    /**
     * Finds an existing conversation between two users or creates a new one.
     * @param senderId The ID of the message sender.
     * @param recipientId The ID of the message recipient.
     * @return The existing or newly created Conversation entity.
     */
    @Override
    @Transactional
    public Conversation findOrCreateConversation(UUID senderId, UUID recipientId) {
        long startTime = System.currentTimeMillis();
        eventLogger.logEvent("FindOrCreateConversation_Start", "senderId=" + senderId + ", recipientId=" + recipientId);

        return conversationRepository.findConversationBetweenUsers(senderId, recipientId)
            .orElseGet(() -> {
                User sender = userRepository.findById(senderId).orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
                User recipient = userRepository.findById(recipientId).orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));

                Conversation newConversation = new Conversation();
                Set<User> participants = new HashSet<>();
                participants.add(sender);
                participants.add(recipient);
                newConversation.setParticipants(participants);
                
                Conversation saved = conversationRepository.save(newConversation);
                eventLogger.logEvent("FindOrCreateConversation_Created", "conversationId=" + saved.getId(), System.currentTimeMillis() - startTime);
                return saved;
            });
    }

    private void verifyUserAccessToConversation(UUID userId, UUID conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
        
        boolean isParticipant = conversation.getParticipants().stream().anyMatch(p -> p.getId().equals(userId));
        if (!isParticipant) {
            eventLogger.logEvent("AccessDenied", "userId=" + userId + ", conversationId=" + conversationId);
            throw new ResourceForbiddenException("Access to this conversation is denied");
        }
    }

    private ConversationDto convertToDto(Conversation conversation, UUID currentUserId) {
        MessageDto lastMessageDto = conversation.getMessages().stream()
                .max((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()))
                .map(this::convertMessageToDto)
                .orElse(null);

        long unreadCount = messageRepository.countUnreadMessages(conversation.getId(), currentUserId);

        return ConversationDto.builder()
                .id(conversation.getId())
                .participants(conversation.getParticipants().stream().map(this::convertUserToDto).collect(Collectors.toList()))
                .lastMessage(lastMessageDto)
                .unreadCount((int) unreadCount)
                .updatedAt(conversation.getUpdatedAt())
                .build();
    }
    
    private MessageDto convertMessageToDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSender().getId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .isRead(message.isRead())
                .build();
    }
    
    private UserDto convertUserToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}