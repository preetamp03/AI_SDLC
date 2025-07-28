package com.example.messaging.repository;

import com.example.messaging.model.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    /**
     * Finds conversations for a given user, with optional search query on participant names.
     * @param userId The ID of the user.
     * @param query The search term.
     * @param pageable The pagination information.
     * @return A page of conversations.
     */
    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.id = :userId " +
           "AND (:query IS NULL OR EXISTS (SELECT p2 FROM c.participants p2 WHERE p2.id != :userId AND lower(p2.name) LIKE lower(concat('%', :query, '%'))))")
    Page<Conversation> findByUserIdWithSearch(@Param("userId") UUID userId, @Param("query") String query, Pageable pageable);

    /**
     * Finds a conversation between two specific users.
     * @param userId1 ID of the first user.
     * @param userId2 ID of the second user.
     * @return An optional containing the conversation if it exists.
     */
    @Query("SELECT c FROM Conversation c JOIN c.participants p1 JOIN c.participants p2 " +
           "WHERE p1.id = :userId1 AND p2.id = :userId2 AND size(c.participants) = 2")
    Optional<Conversation> findConversationBetweenUsers(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);
}