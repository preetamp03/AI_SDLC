package com.example.messaging.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class ConversationTest {

    /**
     * Tests basic instantiation and property setting for the Conversation entity.
     */
    @Test
    void testConversationEntity() {
        Conversation conversation = new Conversation();
        UUID id = UUID.randomUUID();
        conversation.setId(id);

        assertThat(conversation.getId()).isEqualTo(id);
        assertThat(conversation.getParticipants()).isNotNull().isEmpty();
        assertThat(conversation.getMessages()).isNotNull().isEmpty();
    }
}