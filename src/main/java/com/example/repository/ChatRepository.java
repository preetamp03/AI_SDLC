package com.example.repository;

import com.example.model.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    /**
     * Finds a chat between two specific users.
     * @param userId1 ID of the first user.
     * @param userId2 ID of the second user.
     * @return An optional containing the chat if it exists.
     */
    @Query("SELECT c FROM Chat c JOIN c.participants p1 JOIN c.participants p2 " +
           "WHERE p1.user.id = :userId1 AND p2.user.id = :userId2 AND " +
           "(SELECT COUNT(p) FROM c.participants p) = 2")
    Optional<Chat> findChatBetweenUsers(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);

    /**
     * Finds all chats for a given user with pagination and search on recipient name.
     * @param userId The ID of the user.
     * @param searchTerm The search term for recipient name.
     * @param pageable The pagination and sorting information.
     * @return A page of chats.
     */
     @Query(value = "SELECT c FROM Chat c " +
            "JOIN c.participants cp1 " +
            "JOIN c.participants cp2 " +
            "WHERE cp1.user.id = :userId " +
            "AND cp2.user.id != :userId " +
            "AND (:searchTerm IS NULL OR lower(cp2.user.name) LIKE lower(concat('%', :searchTerm, '%')))",
        countQuery = "SELECT count(c) FROM Chat c " +
            "JOIN c.participants cp1 " +
            "JOIN c.participants cp2 " +
            "WHERE cp1.user.id = :userId " +
            "AND cp2.user.id != :userId " +
            "AND (:searchTerm IS NULL OR lower(cp2.user.name) LIKE lower(concat('%', :searchTerm, '%')))")
    Page<Chat> findChatsByUserId(@Param("userId") UUID userId, @Param("searchTerm") String searchTerm, Pageable pageable);
}