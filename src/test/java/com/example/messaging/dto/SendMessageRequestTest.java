package com.example.messaging.dto;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class SendMessageRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    /**
     * Tests a valid SendMessageRequest.
     */
    @Test
    void testValidRequest() {
        SendMessageRequest request = new SendMessageRequest(UUID.randomUUID(), "Hello world");
        assertThat(validator.validate(request)).isEmpty();
    }

    /**
     * Tests a request with a null recipient ID.
     */
    @Test
    void testNullRecipientId() {
        SendMessageRequest request = new SendMessageRequest(null, "Hello world");
        assertThat(validator.validate(request)).hasSize(1);
    }

    /**
     * Tests a request with empty content.
     */
    @Test
    void testEmptyContent() {
        SendMessageRequest request = new SendMessageRequest(UUID.randomUUID(), "");
        assertThat(validator.validate(request)).hasSize(1);
    }
}