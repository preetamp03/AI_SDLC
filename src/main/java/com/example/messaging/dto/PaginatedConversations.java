package com.example.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedConversations {
    private List<ConversationDto> items;
    private int page;
    private int limit;
    private long total;
}