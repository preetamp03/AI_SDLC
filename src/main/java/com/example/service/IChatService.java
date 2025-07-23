package com.example.service;

import com.example.dto.PaginatedChatsDto;
import com.example.model.Chat;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface IChatService {
    /**
     * Retrieves a paginated list of a user's chats.
     * @param userId The ID of the user whose chats to retrieve.
     * @param pageable Pagination and sorting options.
     * @return A paginated list of chat summaries.
     */
    PaginatedChatsDto listUserChats(UUID userId, Pageable pageable);

    /**
     * Marks all messages in a chat as read for a specific user.
     * @param userId The ID of the user.
     * @param chatId The ID of the chat.
     */
    void markChatAsRead(UUID userId, UUID chatId);

    /**
     * Retrieves a chat by its ID and ensures the user is a participant.
     * @param chatId The ID of the chat.
     * @param userId The ID of the user.
     * @return The Chat entity.
     * @throws com.example.exception.ForbiddenException if the user is not a participant.
     * @throws com.example.exception.ResourceNotFoundException if the chat is not found.
     */
    Chat getChatForUser(UUID chatId, UUID userId);
}
```
```java
// src/main/java/com/example/service/IMessageService.java