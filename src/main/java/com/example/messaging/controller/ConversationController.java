package com.example.messaging.controller;

import com.example.messaging.dto.PaginatedConversations;
import com.example.messaging.dto.PaginatedMessages;
import com.example.messaging.security.UserPrincipal;
import com.example.messaging.service.IConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final IConversationService conversationService;

    /**
     * Retrieves a paginated list of conversations for the authenticated user.
     * @param userPrincipal The authenticated user's principal.
     * @param page The page number.
     * @param limit The number of items per page.
     * @param sortBy The field to sort by ('time' or 'seen').
     * @param query A search query to filter conversations.
     * @return A paginated list of conversations.
     */
    @GetMapping
    public ResponseEntity<PaginatedConversations> listConversations(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
        @RequestParam(defaultValue = "time") String sortBy,
        @RequestParam(required = false) String query
    ) {
        PaginatedConversations response = conversationService.findUserConversations(userPrincipal.getId(), page, limit, sortBy, query);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a paginated list of messages for a specific conversation.
     * @param userPrincipal The authenticated user's principal.
     * @param id The ID of the conversation.
     * @param page The page number.
     * @param limit The number of items per page.
     * @return A paginated list of messages.
     */
    @GetMapping("/{id}/messages")
    public ResponseEntity<PaginatedMessages> listMessages(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable UUID id,
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit
    ) {
        PaginatedMessages response = conversationService.findMessagesByConversation(userPrincipal.getId(), id, page, limit);
        return ResponseEntity.ok(response);
    }

    /**
     * Marks a conversation as read by the authenticated user.
     * @param userPrincipal The authenticated user's principal.
     * @param id The ID of the conversation to mark as read.
     * @return A 204 No Content response on success.
     */
    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markConversationAsRead(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable UUID id
    ) {
        conversationService.markAsRead(userPrincipal.getId(), id);
        return ResponseEntity.noContent().build();
    }
}