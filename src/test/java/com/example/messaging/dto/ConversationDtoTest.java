package com.example.messaging.dto;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class ConversationDtoTest {

    /**
     * Tests the builder and getters of the ConversationDto.
     */
    @Test
    void testConversationDto() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        ConversationDto dto = ConversationDto.builder()
                .id(id)
                .participants(Collections.emptyList())
                .unreadCount(5)
                .updatedAt(now)
                .build();

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getParticipants()).isEmpty();
        assertThat(dto.getUnreadCount()).isEqualTo(5);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }
}