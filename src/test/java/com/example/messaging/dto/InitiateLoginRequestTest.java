package com.example.messaging.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class InitiateLoginRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    /**
     * Tests valid InitiateLoginRequest.
     */
    @Test
    void testValidRequest() {
        InitiateLoginRequest request = new InitiateLoginRequest("1234567890", "password123");
        assertThat(validator.validate(request)).isEmpty();
    }

    /**
     * Tests invalid phone number pattern.
     */
    @Test
    void testInvalidPhoneNumber() {
        InitiateLoginRequest request = new InitiateLoginRequest("123", "password123");
        assertThat(validator.validate(request)).hasSize(1);
    }

    /**
     * Tests invalid password length.
     */
    @Test
    void testInvalidPassword() {
        InitiateLoginRequest request = new InitiateLoginRequest("1234567890", "pass");
        assertThat(validator.validate(request)).hasSize(1);
    }
}