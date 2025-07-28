package com.example.messaging.dto;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class MessageDtoTest {

    /**
     * Tests the builder and getters of the MessageDto.
     */
    @Test
    void testMessageDto() {
        UUID id = UUID.randomUUID();
        UUID conversationId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        MessageDto dto = MessageDto.builder()
                .id(id)
                .conversationId(conversationId)
                .senderId(senderId)
                .content("Hello")
                .createdAt(now)
                .isRead(true)
                .build();

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getConversationId()).isEqualTo(conversationId);
        assertThat(dto.getSenderId()).isEqualTo(senderId);
        assertThat(dto.getContent()).isEqualTo("Hello");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.isRead()).isTrue();
    }
}