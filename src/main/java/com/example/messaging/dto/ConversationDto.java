package com.example.messaging.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationDto {
    private UUID id;
    private List<UserDto> participants;
    private MessageDto lastMessage;
    private Integer unreadCount;
    private OffsetDateTime updatedAt;
}