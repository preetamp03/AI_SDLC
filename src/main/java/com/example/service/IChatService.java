package com.example.service;

import com.example.dto.PaginatedChatsDto;
import com.example.model.Chat;

import java.util.UUID;

public interface IChatService {
    /**
     * Retrieves a paginated list of chats for a user.
     * @param userId The user's ID.
     * @param page Page number.
     * @param limit Page size.
     * @param sortBy Sort field.
     * @param sortOrder Sort direction.
     * @param search Search term.
     * @return A paginated list of chat summaries.
     */
    PaginatedChatsDto listUserChats(UUID userId, int page, int limit, String sortBy, String sortOrder, String search);

    /**
     * Marks all messages in a chat as read by a user.
     * @param userId The user's ID.
     * @param chatId The chat's ID.
     */
    void markChatAsRead(UUID userId, UUID chatId);

    /**
     * Gets a chat by ID, ensuring the user is a participant.
     * @param chatId The chat's ID.
     * @param userId The user's ID.
     * @return The Chat entity.
     */
    Chat getChatForUser(UUID chatId, UUID userId);
}
```
```java
// src/main/java/com/example/service/IMessageService.java