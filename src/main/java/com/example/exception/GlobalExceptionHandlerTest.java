package com.example.exception;

import com.example.dto.ErrorDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        webRequest = new ServletWebRequest(request);
    }

    @Test
    void handleApiException_ResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Item not found");
        ResponseEntity<ErrorDto> response = exceptionHandler.handleApiException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatusCode());
        assertEquals("Item not found", response.getBody().getMessage());
        assertEquals("Not Found", response.getBody().getError());
    }

    @Test
    void handleApiException_Unauthorized() {
        UnauthorizedException ex = new UnauthorizedException("Bad credentials");
        ResponseEntity<ErrorDto> response = exceptionHandler.handleApiException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getStatusCode());
        assertEquals("Bad credentials", response.getBody().getMessage());
        assertEquals("Unauthorized", response.getBody().getError());
    }

    @Test
    void handleGlobalException() {
        Exception ex = new Exception("A generic error");
        ResponseEntity<ErrorDto> response = exceptionHandler.handleGlobalException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatusCode());
        assertEquals("An unexpected error occurred. Please try again later.", response.getBody().getMessage());
        assertEquals("Internal Server Error", response.getBody().getError());
    }
}
```
```java
// src/test/java/com/example/logging/LoggingAspectTest.java