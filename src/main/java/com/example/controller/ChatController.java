package com.example.controller;

import com.example.dto.PaginatedChatsDto;
import com.example.security.JwtTokenProvider;
import com.example.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final IChatService chatService;

    /**
     * Retrieves a paginated list of the authenticated user's chats.
     * @param page The page number (1-based).
     * @param limit The number of items per page.
     * @param sortBy The field to sort by.
     * @param sortOrder The sort order (asc/desc).
     * @param authentication The current user's authentication object.
     * @return A paginated list of chat summaries.
     */
    @GetMapping
    public ResponseEntity<PaginatedChatsDto> listChats(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "lastMessageTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            Authentication authentication) {
        
        UUID userId = UUID.fromString(authentication.getName());
        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
        // Note: The actual sorting logic is complex and handled within the service/repository
        // for properties like 'lastMessageTime' which aren't direct fields on the Chat entity.
        // For simplicity, we pass a basic Pageable. The service layer will implement the logic.
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sortBy));

        PaginatedChatsDto result = chatService.listUserChats(userId, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Marks a chat as read for the authenticated user.
     * @param chatId The ID of the chat to mark as read.
     * @param authentication The current user's authentication object.
     * @return A 204 No Content response.
     */
    @PostMapping("/{chat_id}/read")
    public ResponseEntity<Void> markChatAsRead(
            @PathVariable("chat_id") UUID chatId,
            Authentication authentication) {
        
        UUID userId = UUID.fromString(authentication.getName());
        chatService.markChatAsRead(userId, chatId);
        return ResponseEntity.noContent().build();
    }
}