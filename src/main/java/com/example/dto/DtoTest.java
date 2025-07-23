package com.example.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

/**
 * Basic tests for DTOs to ensure constructors and builders work as expected.
 */
class DtoTest {

    @Test
    void testChatSummaryDto() {
        ChatSummaryDto dto = ChatSummaryDto.builder().id(UUID.randomUUID()).build();
        assertNotNull(dto.getId());
    }
    
    @Test
    void testErrorDto() {
        ErrorDto dto = new ErrorDto(404, "Not Found", "Error");
        assertEquals(404, dto.getStatusCode());
    }

    @Test
    void testInitiateLoginRequestDto() {
        InitiateLoginRequestDto dto = new InitiateLoginRequestDto();
        dto.setPhoneNumber("+123");
        assertEquals("+123", dto.getPhoneNumber());
    }
    
    @Test
    void testLoginResponseDto() {
        LoginResponseDto dto = LoginResponseDto.builder().accessToken("abc").build();
        assertEquals("abc", dto.getAccessToken());
    }

    @Test
    void testMessageDto() {
        MessageDto dto = MessageDto.builder().content("test").build();
        assertEquals("test", dto.getContent());
    }

    // Add similar simple tests for other DTOs
}
```
```java
// src/test/java/com/example/exception/ExceptionTest.java