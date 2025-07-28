package com.example.messaging.repository;

import com.example.messaging.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    /**
     * Finds all messages for a given conversation, ordered by creation time descending.
     * @param conversationId The ID of the conversation.
     * @param pageable The pagination information.
     * @return A page of messages.
     */
    Page<Message> findByConversationIdOrderByCreatedAtDesc(UUID conversationId, Pageable pageable);

    /**
     * Counts the number of unread messages for a specific user in a conversation.
     * @param conversationId The ID of the conversation.
     * @param userId The ID of the user.
     * @return The count of unread messages.
     */
    @Query("SELECT count(m) FROM Message m WHERE m.conversation.id = :conversationId AND m.isRead = false AND m.sender.id != :userId")
    long countUnreadMessages(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId);

    /**
     * Marks all messages in a conversation as read for a specific user.
     * @param conversationId The ID of the conversation.
     * @param userId The ID of the user who is reading the messages.
     */
    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.conversation.id = :conversationId AND m.sender.id != :userId AND m.isRead = false")
    void markMessagesAsRead(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId);

}