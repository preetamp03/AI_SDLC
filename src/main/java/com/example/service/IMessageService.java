package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.SendMessageRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IMessageService {
    /**
     * Retrieves a paginated list of messages for a specific chat.
     * @param userId The ID of the authenticated user (for authorization).
     * @param chatId The ID of the chat.
     * @param pageable Pagination options.
     * @return A paginated list of messages.
     */
    PaginatedMessagesDto listChatMessages(UUID userId, UUID chatId, Pageable pageable);

    /**
     * Sends a message from a sender to a recipient.
     * @param senderId The ID of the message sender.
     * @param requestDto The DTO containing recipient ID and message content.
     * @return The created message DTO.
     */
    MessageDto sendMessage(UUID senderId, SendMessageRequestDto requestDto);
}