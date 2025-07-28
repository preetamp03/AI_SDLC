package com.example.messaging.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class VerifyLoginRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    /**
     * Tests a valid VerifyLoginRequest.
     */
    @Test
    void testValidRequest() {
        VerifyLoginRequest request = new VerifyLoginRequest("1234567890", "123456");
        assertThat(validator.validate(request)).isEmpty();
    }

    /**
     * Tests an invalid OTP pattern.
     */
    @Test
    void testInvalidOtp() {
        VerifyLoginRequest request = new VerifyLoginRequest("1234567890", "123");
        assertThat(validator.validate(request)).hasSize(1);
    }
}