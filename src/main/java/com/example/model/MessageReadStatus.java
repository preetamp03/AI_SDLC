package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.Instant;

@Entity
@Table(name = "message_read_status")
@Data
@IdClass(MessageReadStatusId.class)
@EqualsAndHashCode(of = {"message", "user"})
public class MessageReadStatus {
    /*
    mermaid
    erDiagram
        MESSAGE_READ_STATUS {
            UUID messageId FK "Part of PK"
            UUID userId FK "Part of PK"
            timestamp readAt
        }
        MESSAGE }o--|| MESSAGE_READ_STATUS : "status for"
        USER }o--|| MESSAGE_READ_STATUS : "read by"
    */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant readAt;
}