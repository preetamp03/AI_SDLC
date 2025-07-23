package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedChatsDto {
    private List<ChatSummaryDto> data;
    private PaginationDto pagination;
}
```
```java
// src/main/java/com/example/dto/PaginatedMessagesDto.java