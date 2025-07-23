package com.example.repository;

import com.example.model.ChatParticipant;
import com.example.model.ChatParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantId> {
}