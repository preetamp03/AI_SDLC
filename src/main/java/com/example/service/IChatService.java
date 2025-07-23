package com.example.service;

import com.example.dto.PaginatedChatsDto;
import com.example.model.Chat;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface IChatService {
    /**
     * Lists all chats for a given user with pagination and filtering.
     * @param userId The ID of the user.
     * @param pageable Pagination and sorting options.
     * @param search Optional search term for recipient name.
     * @return A paginated result of ChatSummaryDto.
     */
    PaginatedChatsDto listUserChats(UUID userId, Pageable pageable, String search);

    /**
     * Marks all messages in a chat as read for the user.
     * @param userId The ID of the user.
     * @param chatId The ID of the chat.
     */
    void markChatAsRead(UUID userId, UUID chatId);

    /**
     * Retrieves a specific chat if the user is a participant.
     * @param chatId The ID of the chat.
     * @param userId The ID of the user.
     * @return The Chat entity.
     */
    Chat getChatForUser(UUID chatId, UUID userId);
}