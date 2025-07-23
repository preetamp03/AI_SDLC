package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedChatsDto {
    private List<ChatSummaryDto> data;
    private PaginationDto pagination;
}
```
```java
// src/main/java/com/example/dto/PaginatedMessagesDto.java