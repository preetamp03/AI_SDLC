package com.example.repository;

import com.example.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IMessageRepository extends JpaRepository<Message, UUID> {

    /**
     * Finds all messages for a given chat ID, ordered by creation time.
     * @param chatId The ID of the chat.
     * @param pageable Pagination information.
     * @return A page of messages.
     */
    Page<Message> findByChatIdOrderByCreatedAtDesc(UUID chatId, Pageable pageable);

    /**
     * Retrieves the latest message for a given chat ID.
     * @param chatId The ID of the chat.
     * @return An Optional containing the last message.
     */
    Optional<Message> findTopByChatIdOrderByCreatedAtDesc(UUID chatId);

    /**
     * Counts unread messages for a user in a specific chat.
     * This is a conceptual query; a real implementation would likely use a MessageReadStatus table.
     * @param chatId The ID of the chat.
     * @param userId The ID of the user.
     * @return The count of unread messages.
     */
    @Query("SELECT COUNT(m) FROM Message m WHERE m.chat.id = :chatId AND m.sender.id != :userId") // Simplified logic
    long countUnreadMessages(@Param("chatId") UUID chatId, @Param("userId") UUID userId);
}
```
```java
// src/main/java/com/example/repository/IOtpRepository.java