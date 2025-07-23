package com.example.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class MessageDto {
    private UUID id;
    private UUID chatId;
    private UUID senderId;
    private String content;
    private Instant timestamp;
}
```
```java
// src/main/java/com/example/dto/PaginationDto.java