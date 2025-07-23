package com.example.service;

import com.example.dto.*;
import com.example.exception.ForbiddenException;
import com.example.exception.ResourceNotFoundException;
import com.example.logging.LogExecutionTime;
import com.example.model.Chat;
import com.example.model.ChatParticipant;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.IChatRepository;
import com.example.repository.IMessageRepository;
import com.example.repository.IMessageReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * <!--
 * mermaid
 * sequenceDiagram
 *   participant User as Client
 *   participant ChatController
 *   participant ChatService
 *   participant ChatRepository
 *   participant MessageRepository
 *
 *   User->>+ChatController: GET /chats
 *   ChatController->>+ChatService: listUserChats(userId, pageable)
 *   ChatService->>+ChatRepository: findChatsByUserId(userId, pageable)
 *   ChatRepository-->>-ChatService: Page<Chat>
 *   loop for each chat
 *     ChatService->>MessageRepository: countUnreadMessages(...)
 *     MessageRepository-->>ChatService: unreadCount
 *   end
 *   ChatService-->>-ChatController: PaginatedChatsDto
 *   ChatController-->>-User: 200 OK (JSON Payload)
 * -->
 */

@Service
@RequiredArgsConstructor
public class ChatService implements IChatService {

    private final IChatRepository chatRepository;
    private final IMessageRepository messageRepository;
    private final IMessageReadStatusRepository messageReadStatusRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    @Transactional(readOnly = true)
    public PaginatedChatsDto listUserChats(UUID userId, Pageable pageable) {
        Page<Chat> chatPage = chatRepository.findChatsByUserId(userId, pageable);

        List<ChatSummaryDto> chatSummaries = chatPage.getContent().stream()
                .map(chat -> convertToChatSummaryDto(chat, userId))
                .collect(Collectors.toList());

        PaginationDto paginationDto = new PaginationDto(
                chatPage.getNumber() + 1,
                chatPage.getTotalPages(),
                chatPage.getSize(),
                chatPage.getTotalElements(),
                chatPage.hasNext(),
                chatPage.hasPrevious()
        );

        return new PaginatedChatsDto(chatSummaries, paginationDto);
    }
    
    /**
     * Converts a Chat entity to a ChatSummaryDto.
     * @param chat The Chat entity.
     * @param currentUserId The ID of the user requesting the summary.
     * @return A ChatSummaryDto.
     */
    private ChatSummaryDto convertToChatSummaryDto(Chat chat, UUID currentUserId) {
        // Find the other participant (the recipient)
        User recipient = chat.getParticipants().stream()
                .map(ChatParticipant::getUser)
                .filter(user -> !user.getId().equals(currentUserId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Chat has no other participant."));

        UserSummaryDto recipientDto = UserSummaryDto.builder()
                .id(recipient.getId())
                .name(recipient.getName())
                .avatarUrl(recipient.getAvatarUrl())
                .build();
        
        // Get the last message
        Optional<Message> lastMessageOpt = chat.getMessages().stream()
                                               .max((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()));

        MessageSummaryDto lastMessageDto = lastMessageOpt.map(message -> MessageSummaryDto.builder()
                .content(message.getContent())
                .timestamp(message.getCreatedAt())
                .build()).orElse(null);

        long unreadCount = messageRepository.countUnreadMessagesForUserInChat(chat.getId(), currentUserId);
        
        return ChatSummaryDto.builder()
                .id(chat.getId())
                .recipient(recipientDto)
                .lastMessage(lastMessageDto)
                .unreadCount(unreadCount)
                .isRead(unreadCount == 0)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    @Transactional
    public void markChatAsRead(UUID userId, UUID chatId) {
        Chat chat = getChatForUser(chatId, userId);
        // This is a simplified implementation. A real one would be more efficient.
        // It marks all unread messages for the user in this chat as read.
        messageReadStatusRepository.markAllAsReadInChatForUser(chatId, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Chat getChatForUser(UUID chatId, UUID userId) {
        return chatRepository.findByIdAndParticipant(chatId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with ID: " + chatId));
    }
}