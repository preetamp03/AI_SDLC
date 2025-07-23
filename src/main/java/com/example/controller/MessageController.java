package com.example.controller;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.SendMessageRequestDto;
import com.example.model.User;
import com.example.service.IMessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private static final Logger log = LogManager.getLogger(MessageController.class);
    private final IMessageService messageService;

    /**
     * Sends a new message to a recipient.
     * @param user The authenticated user (sender).
     * @param request DTO containing recipient ID and message content.
     * @return The created message DTO with a 201 Created status.
     */
    @PostMapping("/messages")
    public ResponseEntity<MessageDto> sendMessage(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SendMessageRequestDto request) {
        log.info("User {} sending message to recipient {}", user.getId(), request.getRecipientId());
        MessageDto createdMessage = messageService.sendMessage(user.getId(), request);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }

    /**
     * Lists messages for a specific chat with pagination.
     * @param user The authenticated user.
     * @param chatId The ID of the chat to retrieve messages from.
     * @param page The page number for pagination.
     * @param limit The number of messages per page.
     * @return A paginated list of messages.
     */
    @GetMapping("/chats/{chat_id}/messages")
    public ResponseEntity<PaginatedMessagesDto> listChatMessages(
            @AuthenticationPrincipal User user,
            @PathVariable("chat_id") UUID chatId,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit) {
        log.info("User {} listing messages for chat {} with params: page={}, limit={}",
                user.getId(), chatId, page, limit);
        PaginatedMessagesDto messages = messageService.listChatMessages(user.getId(), chatId, page, limit);
        return ResponseEntity.ok(messages);
    }
}
```
```java
// src/main/java/com/example/dto/ChatSummaryDto.java