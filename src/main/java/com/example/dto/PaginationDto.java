package com.example.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class PaginationDto {
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private long totalCount;
    private boolean hasNext;
    private boolean hasPrevious;

    /**
     * Creates a PaginationDto from a Spring Data Page object.
     * @param page The Page object.
     * @return A new PaginationDto instance.
     */
    public static PaginationDto fromPage(Page<?> page) {
        return PaginationDto.builder()
                .currentPage(page.getNumber() + 1)
                .totalPages(page.getTotalPages())
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
```
```java
// src/main/java/com/example/dto/SendMessageRequestDto.java