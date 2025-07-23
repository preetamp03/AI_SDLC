package com.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ChatSummaryDto {
    private UUID id;
    private UserSummaryDto recipient;
    private MessageSummaryDto lastMessage;
    private long unreadCount;
    private boolean isRead;
}
```
```java
// src/main/java/com/example/dto/MessageDto.java