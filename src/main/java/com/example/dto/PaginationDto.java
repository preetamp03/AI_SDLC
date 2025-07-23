package com.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationDto {
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private long totalCount;
    private boolean hasNext;
    private boolean hasPrevious;
}