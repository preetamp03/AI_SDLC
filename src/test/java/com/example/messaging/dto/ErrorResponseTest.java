package com.example.messaging.dto;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    /**
     * Tests the builder and getters of the ErrorResponse DTO.
     */
    @Test
    void testErrorResponse() {
        ErrorResponse response = ErrorResponse.builder()
                .statusCode(404)
                .message("Not Found")
                .error("The requested resource was not found")
                .errors(Collections.singletonList("ID does not exist"))
                .build();

        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("Not Found");
        assertThat(response.getError()).isEqualTo("The requested resource was not found");
        assertThat(response.getErrors()).containsExactly("ID does not exist");
    }
}