package com.example.messaging.dto;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;

class PaginatedConversationsTest {

    /**
     * Tests the constructor and getters of the PaginatedConversations DTO.
     */
    @Test
    void testPaginatedConversations() {
        PaginatedConversations paginated = new PaginatedConversations(Collections.emptyList(), 1, 10, 100L);

        assertThat(paginated.getItems()).isEmpty();
        assertThat(paginated.getPage()).isEqualTo(1);
        assertThat(paginated.getLimit()).isEqualTo(10);
        assertThat(paginated.getTotal()).isEqualTo(100L);
    }
}