package com.example.repository;

import com.example.model.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IChatRepository extends JpaRepository<Chat, UUID> {

    /**
     * Finds chats for a specific user, optionally filtering by recipient name.
     * @param userId The ID of the user.
     * @param searchTerm The search term for recipient name (can be null).
     * @param pageable Pagination and sorting information.
     * @return A page of chats.
     */
    @Query("SELECT c FROM Chat c JOIN c.participants p1 JOIN c.participants p2 " +
           "WHERE p1.user.id = :userId AND p2.user.id != :userId " +
           "AND (:searchTerm IS NULL OR lower(p2.user.name) LIKE lower(concat('%', :searchTerm, '%')))")
    Page<Chat> findChatsByUserId(@Param("userId") UUID userId, @Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Finds a chat by its ID ensuring a specific user is a participant.
     * @param chatId The ID of the chat.
     * @param userId The ID of the user who must be a participant.
     * @return An Optional containing the chat if found.
     */
    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE c.id = :chatId AND p.user.id = :userId")
    Optional<Chat> findByIdAndParticipant(@Param("chatId") UUID chatId, @Param("userId") UUID userId);

    /**
     * Finds an existing chat between two specific users.
     * @param userId1 The ID of the first user.
     * @param userId2 The ID of the second user.
     * @return An Optional containing the chat if it exists.
     */
    @Query("SELECT c FROM Chat c WHERE " +
           "(EXISTS (SELECT p FROM c.participants p WHERE p.user.id = :userId1)) AND " +
           "(EXISTS (SELECT p FROM c.participants p WHERE p.user.id = :userId2)) AND " +
           "(SELECT count(p) FROM c.participants p) = 2")
    Optional<Chat> findChatBetweenUsers(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);
}
```
```java
// src/main/java/com/example/repository/IMessageRepository.java