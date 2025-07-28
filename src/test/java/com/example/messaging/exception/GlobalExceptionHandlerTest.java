package com.example.messaging.exception;

import com.example.messaging.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * Tests handling of ResourceNotFoundException.
     */
    @Test
    void handleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");
        ResponseEntity<ErrorResponse> responseEntity = handler.handleResourceNotFoundException(ex);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("User not found");
        assertThat(responseEntity.getBody().getStatusCode()).isEqualTo(404);
    }

    /**
     * Tests handling of InvalidCredentialsException.
     */
    @Test
    void handleInvalidCredentialsException() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Wrong password");
        ResponseEntity<ErrorResponse> responseEntity = handler.handleInvalidCredentialsException(ex);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("Wrong password");
        assertThat(responseEntity.getBody().getStatusCode()).isEqualTo(401);
    }

    /**
     * Tests handling of ResourceForbiddenException.
     */
    @Test
    void handleResourceForbiddenException() {
        ResourceForbiddenException ex = new ResourceForbiddenException("Access denied");
        ResponseEntity<ErrorResponse> responseEntity = handler.handleResourceForbiddenException(ex);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("Access denied");
        assertThat(responseEntity.getBody().getStatusCode()).isEqualTo(403);
    }

    /**
     * Tests handling of generic exceptions.
     */
    @Test
    void handleGlobalException() {
        Exception ex = new RuntimeException("Something went wrong");
        ResponseEntity<ErrorResponse> responseEntity = handler.handleGlobalException(ex);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("An internal server error occurred");
        assertThat(responseEntity.getBody().getStatusCode()).isEqualTo(500);
    }
}