package com.example.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

/**
 * Basic tests for DTOs to ensure builders and getters/setters work.
 */
class DtoTest {

    @Test
    void testLoginResponseDto() {
        LoginResponseDto dto = new LoginResponseDto("access", "refresh");
        assertEquals("access", dto.getAccessToken());
        assertEquals("refresh", dto.getRefreshToken());
    }

    @Test
    void testChatSummaryDtoBuilder() {
        UUID id = UUID.randomUUID();
        UserSummaryDto user = UserSummaryDto.builder().id(UUID.randomUUID()).name("Test").build();
        MessageSummaryDto msg = MessageSummaryDto.builder().content("Hi").timestamp(Instant.now()).build();

        ChatSummaryDto dto = ChatSummaryDto.builder()
                .id(id)
                .recipient(user)
                .lastMessage(msg)
                .unreadCount(1)
                .isRead(false)
                .build();
        
        assertEquals(id, dto.getId());
        assertEquals("Test", dto.getRecipient().getName());
        assertEquals("Hi", dto.getLastMessage().getContent());
        assertEquals(1, dto.getUnreadCount());
        assertFalse(dto.isRead());
    }
    
    @Test
    void testPaginatedChatsDto() {
        PaginationDto pagination = new PaginationDto();
        PaginatedChatsDto dto = new PaginatedChatsDto(Collections.emptyList(), pagination);
        assertNotNull(dto.getData());
        assertNotNull(dto.getPagination());
    }
}
```
```java
// src/test/java/com/example/exception/GlobalExceptionHandlerTest.java