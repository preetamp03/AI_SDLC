package com.example.controller;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.SendMessageRequest;
import com.example.model.User;
import com.example.service.IMessageService;
import com.example.util.PaginationUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class MessageController {

    private final IMessageService messageService;

    /**
     * Retrieves a paginated list of messages for a specific chat.
     * @param currentUser The authenticated user.
     * @param chatId The ID of the chat.
     * @param page The page number to retrieve.
     * @param limit The number of items per page.
     * @return A paginated list of messages.
     */
    @GetMapping("/chats/{chat_id}/messages")
    public ResponseEntity<PaginatedMessagesDto> listChatMessages(
        @AuthenticationPrincipal User currentUser,
        @PathVariable("chat_id") UUID chatId,
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit
    ) {
        Pageable pageable = PaginationUtil.createPageable(page, limit, "createdAt", "desc");
        PaginatedMessagesDto result = messageService.listChatMessages(currentUser.getId(), chatId, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Sends a new message to a recipient.
     * @param currentUser The authenticated user sending the message.
     * @param request The request body containing recipient ID and content.
     * @return The created message.
     */
    @PostMapping("/messages")
    public ResponseEntity<MessageDto> sendMessage(
        @AuthenticationPrincipal User currentUser,
        @Valid @RequestBody SendMessageRequest request
    ) {
        MessageDto createdMessage = messageService.sendMessage(currentUser.getId(), request);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }
}