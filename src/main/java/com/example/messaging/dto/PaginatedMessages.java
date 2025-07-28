package com.example.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedMessages {
    private List<MessageDto> items;
    private int page;
    private int limit;
    private long total;
}