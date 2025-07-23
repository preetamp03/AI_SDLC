package com.example.util;

import com.example.dto.PaginationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PaginationUtil {

    private PaginationUtil() {}

    /**
     * Creates a Pageable object from request parameters.
     * @param page The page number (1-based).
     * @param limit The page size.
     * @param sortBy The field to sort by.
     * @param sortOrder The sort direction ("asc" or "desc").
     * @return A Pageable object.
     */
    public static Pageable createPageable(int page, int limit, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        return PageRequest.of(page - 1, limit, sort);
    }

    /**
     * Creates a PaginationDto from a Page object.
     * @param page The Page object from a repository query.
     * @return A PaginationDto.
     */
    public static <T> PaginationDto createPaginationDto(Page<T> page) {
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