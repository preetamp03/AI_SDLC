package com.example.repository;

import com.example.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    /**
     * Finds messages by chat ID with pagination.
     * @param chatId The ID of the chat.
     * @param pageable The pagination and sorting information.
     * @return A page of messages.
     */
    Page<Message> findByChatId(UUID chatId, Pageable pageable);

    /**
     * Counts unread messages for a user in a specific chat.
     * @param chatId The ID of the chat.
     * @param userId The ID of the user.
     * @return The count of unread messages.
     */
    @Query("SELECT COUNT(m) FROM Message m " +
           "WHERE m.chat.id = :chatId AND m.sender.id != :userId AND NOT EXISTS " +
           "(SELECT mrs FROM MessageReadStatus mrs WHERE mrs.message = m AND mrs.user.id = :userId)")
    long countUnreadMessages(@Param("chatId") UUID chatId, @Param("userId") UUID userId);

    /**
     * Finds the last message in a chat.
     * @param chatId The ID of the chat.
     * @return An optional containing the last message.
     */
    Optional<Message> findTopByChatIdOrderByCreatedAtDesc(UUID chatId);
}