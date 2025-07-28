package com.example.messaging.controller;

import com.example.messaging.dto.MessageDto;
import com.example.messaging.dto.SendMessageRequest;
import com.example.messaging.security.UserPrincipal;
import com.example.messaging.service.IMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;

    /**
     * Sends a new message to a recipient.
     * @param userPrincipal The authenticated sender's principal.
     * @param request The request body containing recipient ID and message content.
     * @return The created message DTO with a 201 Created status.
     */
    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody SendMessageRequest request) {
        MessageDto createdMessage = messageService.createMessage(userPrincipal.getId(), request);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }
}