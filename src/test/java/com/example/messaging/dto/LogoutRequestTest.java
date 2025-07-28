package com.example.messaging.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class LogoutRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    /**
     * Tests valid LogoutRequest.
     */
    @Test
    void testValidRequest() {
        LogoutRequest request = new LogoutRequest("some.refresh.token");
        assertThat(validator.validate(request)).isEmpty();
    }

    /**
     * Tests empty refresh token.
     */
    @Test
    void testEmptyRefreshToken() {
        LogoutRequest request = new LogoutRequest("");
        assertThat(validator.validate(request)).hasSize(1);
    }
}