package com.example.messaging.service;

import com.example.messaging.dto.MessageDto;
import com.example.messaging.dto.SendMessageRequest;
import java.util.UUID;

public interface IMessageService {
    /**
     * Creates and saves a new message, potentially creating a new conversation.
     * @param senderId The ID of the user sending the message.
     * @param request DTO containing the recipient ID and content.
     * @return A DTO representation of the newly created message.
     */
    MessageDto createMessage(UUID senderId, SendMessageRequest request);
}