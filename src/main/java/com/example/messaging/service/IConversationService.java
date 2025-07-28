package com.example.messaging.service;

import com.example.messaging.dto.PaginatedConversations;
import com.example.messaging.dto.PaginatedMessages;
import com.example.messaging.model.Conversation;

import java.util.UUID;

public interface IConversationService {
    /**
     * Finds all conversations for a specific user with pagination, sorting, and search.
     * @param userId The user's ID.
     * @param page The page number.
     * @param limit The number of items per page.
     * @param sortBy The sorting criteria.
     * @param query The search query.
     * @return A paginated response of Conversation DTOs.
     */
    PaginatedConversations findUserConversations(UUID userId, int page, int limit, String sortBy, String query);

    /**
     * Finds all messages within a specific conversation with pagination.
     * @param userId The ID of the user making the request.
     * @param conversationId The conversation's ID.
     * @param page The page number.
     * @param limit The number of items per page.
     * @return A paginated response of Message DTOs.
     */
    PaginatedMessages findMessagesByConversation(UUID userId, UUID conversationId, int page, int limit);

    /**
     * Marks all messages in a conversation as read by the user.
     * @param userId The ID of the user making the request.
     * @param conversationId The conversation's ID.
     */
    void markAsRead(UUID userId, UUID conversationId);
    
    /**
     * Finds an existing conversation between two users or creates a new one.
     * @param senderId The ID of the message sender.
     * @param recipientId The ID of the message recipient.
     * @return The existing or newly created Conversation entity.
     */
    Conversation findOrCreateConversation(UUID senderId, UUID recipientId);
}