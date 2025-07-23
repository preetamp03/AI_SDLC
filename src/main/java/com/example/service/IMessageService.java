package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.SendMessageRequest;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface IMessageService {
    /**
     * Lists all messages for a specific chat with pagination.
     * @param userId The ID of the user requesting the messages.
     * @param chatId The ID of the chat.
     * @param pageable Pagination options.
     * @return A paginated result of MessageDto.
     */
    PaginatedMessagesDto listChatMessages(UUID userId, UUID chatId, Pageable pageable);

    /**
     * Sends a message from a sender to a recipient.
     * @param senderId The ID of the user sending the message.
     * @param request The request DTO containing recipient ID and content.
     * @return The created MessageDto.
     */
    MessageDto sendMessage(UUID senderId, SendMessageRequest request);
}