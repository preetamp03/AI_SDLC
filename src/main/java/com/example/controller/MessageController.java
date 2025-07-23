package com.example.controller;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.SendMessageRequestDto;
import com.example.service.IMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;

    /**
     * Sends a new message.
     * @param requestDto The request body containing the message details.
     * @param authentication The current user's authentication object.
     * @return A 201 Created response with the new message object.
     */
    @PostMapping("/messages")
    public ResponseEntity<MessageDto> sendMessage(
            @Valid @RequestBody SendMessageRequestDto requestDto,
            Authentication authentication) {
        
        UUID senderId = UUID.fromString(authentication.getName());
        MessageDto createdMessage = messageService.sendMessage(senderId, requestDto);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated list of messages for a specific chat.
     * @param chatId The ID of the chat.
     * @param page The page number (1-based).
     * @param limit The number of items per page.
     * @param authentication The current user's authentication object.
     * @return A paginated list of messages.
     */
    @GetMapping("/chats/{chat_id}/messages")
    public ResponseEntity<PaginatedMessagesDto> listChatMessages(
            @PathVariable("chat_id") UUID chatId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int limit,
            Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        PaginatedMessagesDto result = messageService.listChatMessages(userId, chatId, pageable);
        return ResponseEntity.ok(result);
    }
}