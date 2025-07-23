package com.example.controller;

import com.example.dto.PaginatedChatsDto;
import com.example.model.User;
import com.example.service.IChatService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class ChatController {

    private static final Logger log = LogManager.getLogger(ChatController.class);
    private final IChatService chatService;

    /**
     * Lists all chats for the authenticated user with pagination and sorting.
     * @param user The authenticated user principal.
     * @param page The page number for pagination.
     * @param limit The number of items per page.
     * @param sortBy The field to sort by.
     * @param sortOrder The sort direction.
     * @param search A search term for recipient names.
     * @return A paginated list of the user's chats.
     */
    @GetMapping
    public ResponseEntity<PaginatedChatsDto> listChats(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
            @RequestParam(defaultValue = "lastMessageTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String search) {
        log.info("User {} listing chats with params: page={}, limit={}, sortBy={}, sortOrder={}, search={}",
                user.getId(), page, limit, sortBy, sortOrder, search);
        PaginatedChatsDto chats = chatService.listUserChats(user.getId(), page, limit, sortBy, sortOrder, search);
        return ResponseEntity.ok(chats);
    }

    /**
     * Marks all messages in a specific chat as read for the authenticated user.
     * @param user The authenticated user principal.
     * @param chatId The ID of the chat to mark as read.
     * @return A 204 No Content response on success.
     */
    @PostMapping("/{chat_id}/read")
    public ResponseEntity<Void> markChatAsRead(
            @AuthenticationPrincipal User user,
            @PathVariable("chat_id") UUID chatId) {
        log.info("User {} marking chat {} as read", user.getId(), chatId);
        chatService.markChatAsRead(user.getId(), chatId);
        return ResponseEntity.noContent().build();
    }
}
```
```java
// src/main/java/com/example/controller/MessageController.java