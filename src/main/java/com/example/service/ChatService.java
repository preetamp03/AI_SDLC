package com.example.service;

import com.example.dto.ChatSummaryDto;
import com.example.dto.MessageSummaryDto;
import com.example.dto.PaginatedChatsDto;
import com.example.dto.PaginationDto;
import com.example.dto.UserSummaryDto;
import com.example.exception.ForbiddenException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Chat;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.IChatRepository;
import com.example.repository.IMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService implements IChatService {

    private final IChatRepository chatRepository;
    private final IMessageRepository messageRepository;

    /**
     * Lists chats for a given user with pagination, sorting, and search.
     * @param userId The ID of the user.
     * @param page Page number.
     * @param limit Page size.
     * @param sortBy Field to sort by.
     * @param sortOrder Sort direction ('asc' or 'desc').
     * @param search Search term for recipient name.
     * @return A paginated list of chat summaries.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedChatsDto listUserChats(UUID userId, int page, int limit, String sortBy, String sortOrder, String search) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        // Note: Sorting by lastMessageTime requires a more complex query or denormalization
        // For simplicity, we sort by chat update time.
        String sortField = "updatedAt";
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sortField));

        Page<Chat> chatPage = chatRepository.findChatsByUserId(userId, search, pageable);

        List<ChatSummaryDto> chatSummaries = chatPage.getContent().stream()
                .map(chat -> convertToChatSummaryDto(chat, userId))
                .collect(Collectors.toList());

        return new PaginatedChatsDto(chatSummaries, PaginationDto.fromPage(chatPage));
    }

    /**
     * Marks a chat as read for a specific user.
     * This is a conceptual implementation. A real one would update MessageReadStatus records.
     * @param userId The ID of the user.
     * @param chatId The ID of the chat.
     */
    @Override
    @Transactional
    public void markChatAsRead(UUID userId, UUID chatId) {
        Chat chat = getChatForUser(chatId, userId);
        // In a real system, you would iterate through unread messages and create
        // MessageReadStatus entries. For this example, this action is conceptual.
        // E.g., messageRepository.markMessagesAsRead(chatId, userId);
    }

    /**
     * Retrieves a chat by its ID, ensuring the user is a participant.
     * @param chatId The ID of the chat.
     * @param userId The ID of the user.
     * @return The Chat entity.
     * @throws ResourceNotFoundException if the chat doesn't exist.
     * @throws ForbiddenException if the user is not a participant.
     */
    @Override
    public Chat getChatForUser(UUID chatId, UUID userId) {
        return chatRepository.findByIdAndParticipant(chatId, userId)
                .orElseThrow(() -> new ForbiddenException("You do not have permission to access this chat."));
    }

    private ChatSummaryDto convertToChatSummaryDto(Chat chat, UUID currentUserId) {
        User recipient = chat.getParticipants().stream()
                .map(p -> p.getUser())
                .filter(u -> !u.getId().equals(currentUserId))
                .findFirst()
                .orElse(null); // Should not happen in a 2-person chat

        Message lastMessage = messageRepository.findTopByChatIdOrderByCreatedAtDesc(chat.getId()).orElse(null);

        return ChatSummaryDto.builder()
                .id(chat.getId())
                .recipient(recipient != null ? UserSummaryDto.builder()
                        .id(recipient.getId())
                        .name(recipient.getName())
                        .avatarUrl(recipient.getAvatarUrl())
                        .build() : null)
                .lastMessage(lastMessage != null ? MessageSummaryDto.builder()
                        .content(lastMessage.getContent())
                        .timestamp(lastMessage.getCreatedAt())
                        .build() : null)
                .unreadCount((int) messageRepository.countUnreadMessages(chat.getId(), currentUserId)) // Simplified
                .isRead(true) // Simplified
                .build();
    }
}
```
```java
// src/main/java/com/example/service/IAuthService.java