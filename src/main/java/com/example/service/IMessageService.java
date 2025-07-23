package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.SendMessageRequestDto;

import java.util.UUID;

public interface IMessageService {
    /**
     * Retrieves a paginated list of messages for a specific chat.
     * @param userId The ID of the user requesting the messages.
     * @param chatId The ID of the chat.
     * @param page Page number.
     * @param limit Page size.
     * @return A paginated list of messages.
     */
    PaginatedMessagesDto listChatMessages(UUID userId, UUID chatId, int page, int limit);

    /**
     * Sends a message from a sender to a recipient.
     * @param senderId The ID of the sender.
     * @param request DTO containing recipient ID and content.
     * @return The DTO of the created message.
     */
    MessageDto sendMessage(UUID senderId, SendMessageRequestDto request);
}
```
```java
// src/main/java/com/example/service/IOtpService.java