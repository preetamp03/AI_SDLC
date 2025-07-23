package com.example.controller;

import com.example.dto.PaginatedChatsDto;
import com.example.model.User;
import com.example.service.IChatService;
import com.example.util.PaginationUtil;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
@Validated
public class ChatController {

    private final IChatService chatService;

    /**
     * Retrieves a paginated list of the current user's chats.
     * @param currentUser The authenticated user.
     * @param page The page number to retrieve.
     * @param limit The number of items per page.
     * @param sortBy The field to sort by.
     * @param sortOrder The sort direction.
     * @param search A search term for recipient name.
     * @return A paginated list of chat summaries.
     */
    @GetMapping
    public ResponseEntity<PaginatedChatsDto> listChats(
        @AuthenticationPrincipal User currentUser,
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
        @RequestParam(defaultValue = "lastMessageTime") String sortBy,
        @RequestParam(defaultValue = "desc") String sortOrder,
        @RequestParam(required = false) String search
    ) {
        Pageable pageable = PaginationUtil.createPageable(page, limit, sortBy, sortOrder);
        PaginatedChatsDto result = chatService.listUserChats(currentUser.getId(), pageable, search);
        return ResponseEntity.ok(result);
    }

    /**
     * Marks all messages in a chat as read for the current user.
     * @param currentUser The authenticated user.
     * @param chatId The ID of the chat to mark as read.
     * @return A response entity with no content.
     */
    @PostMapping("/{chat_id}/read")
    public ResponseEntity<Void> markChatAsRead(
        @AuthenticationPrincipal User currentUser,
        @PathVariable("chat_id") UUID chatId
    ) {
        chatService.markChatAsRead(currentUser.getId(), chatId);
        return ResponseEntity.noContent().build();
    }
}