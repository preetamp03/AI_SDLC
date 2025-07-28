package com.example.messaging.service;

import com.example.messaging.dto.MessageDto;
import com.example.messaging.dto.SendMessageRequest;
import com.example.messaging.exception.ResourceNotFoundException;
import com.example.messaging.model.Conversation;
import com.example.messaging.model.Message;
import com.example.messaging.model.User;
import com.example.messaging.repository.MessageRepository;
import com.example.messaging.repository.UserRepository;
import com.example.messaging.util.EventLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final IConversationService conversationService;
    private final EventLogger eventLogger;

    /**
     * Creates and saves a new message, potentially creating a new conversation.
     * @param senderId The ID of the user sending the message.
     * @param request DTO containing the recipient ID and content.
     * @return A DTO representation of the newly created message.
     */
    @Override
    @Transactional
    public MessageDto createMessage(UUID senderId, SendMessageRequest request) {
        long startTime = System.currentTimeMillis();
        eventLogger.logEvent("CreateMessage_Start", "senderId=" + senderId + ", recipientId=" + request.getRecipientId());

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        
        // Ensure recipient exists
        if (!userRepository.existsById(request.getRecipientId())) {
             throw new ResourceNotFoundException("Recipient not found");
        }

        Conversation conversation = conversationService.findOrCreateConversation(senderId, request.getRecipientId());

        Message message = new Message();
        message.setSender(sender);
        message.setConversation(conversation);
        message.setContent(request.getContent());
        message.setRead(false);

        Message savedMessage = messageRepository.save(message);

        // Update conversation's updatedAt timestamp
        conversation.setUpdatedAt(savedMessage.getCreatedAt());
        
        eventLogger.logEvent("CreateMessage_Success", "messageId=" + savedMessage.getId(), System.currentTimeMillis() - startTime);
        return convertToDto(savedMessage);
    }
    
    private MessageDto convertToDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSender().getId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .isRead(message.isRead())
                .build();
    }
}