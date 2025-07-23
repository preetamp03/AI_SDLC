package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_participants")
@Data
@NoArgsConstructor
public class ChatParticipant {
    @EmbeddedId
    private ChatParticipantId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    public ChatParticipant(Chat chat, User user) {
        this.chat = chat;
        this.user = user;
        this.id = new ChatParticipantId(chat.getId(), user.getId());
    }
}