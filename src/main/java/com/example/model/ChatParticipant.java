package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "chat_participants")
@Data
@IdClass(ChatParticipantId.class)
@EqualsAndHashCode(of = {"chat", "user"})
public class ChatParticipant {
    /*
    mermaid
    erDiagram
        CHAT_PARTICIPANT {
            UUID chatId FK "Part of PK"
            UUID userId FK "Part of PK"
        }
        CHAT }o--|| CHAT_PARTICIPANT : "participates in"
        USER }o--|| CHAT_PARTICIPANT : "is participant"
    */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}