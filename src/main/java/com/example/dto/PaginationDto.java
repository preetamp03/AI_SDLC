package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto {
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private long totalCount;
    private boolean hasNext;
    private boolean hasPrevious;
}
```
```java
// src/main/java/com/example/dto/PaginatedChatsDto.java