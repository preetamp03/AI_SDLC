package com.example.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class MessageSummaryDto {
    private String content;
    private Instant timestamp;
}
```
```java
// src/main/java/com/example/dto/PaginatedChatsDto.java