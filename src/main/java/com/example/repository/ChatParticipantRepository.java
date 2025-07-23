package com.example.repository;

import com.example.model.ChatParticipant;
import com.example.model.ChatParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantId> {
    /**
     * Checks if a user is a participant in a given chat.
     * @param chatId The ID of the chat.
     * @param userId The ID of the user.
     * @return true if the user is a participant, false otherwise.
     */
    boolean existsByChatIdAndUserId(UUID chatId, UUID userId);
}