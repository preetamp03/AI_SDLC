package com.example.messaging.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {

    /**
     * Tests basic instantiation and property setting for the Message entity.
     */
    @Test
    void testMessageEntity() {
        Message message = new Message();
        UUID id = UUID.randomUUID();
        message.setId(id);
        message.setContent("Hello");
        message.setRead(true);

        assertThat(message.getId()).isEqualTo(id);
        assertThat(message.getContent()).isEqualTo("Hello");
        assertThat(message.isRead()).isTrue();
    }
}