package com.example.messaging.dto;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;

class PaginatedMessagesTest {

    /**
     * Tests the constructor and getters of the PaginatedMessages DTO.
     */
    @Test
    void testPaginatedMessages() {
        PaginatedMessages paginated = new PaginatedMessages(Collections.emptyList(), 2, 20, 50L);

        assertThat(paginated.getItems()).isEmpty();
        assertThat(paginated.getPage()).isEqualTo(2);
        assertThat(paginated.getLimit()).isEqualTo(20);
        assertThat(paginated.getTotal()).isEqualTo(50L);
    }
}