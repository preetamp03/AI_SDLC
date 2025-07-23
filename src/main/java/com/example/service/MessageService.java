package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.PaginationDto;
import com.example.dto.SendMessageRequestDto;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.*;
import com.example.repository.IChatRepository;
import com.example.repository.IMessageRepository;
import com.example.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final IMessageRepository messageRepository;
    private final IChatRepository chatRepository;
    private final IUserRepository userRepository;
    private final IChatService chatService;

    /**
     * Lists messages for a given chat, ensuring user has access.
     * @param userId The ID of the user making the request.
     * @param chatId The ID of the chat.
     * @param page The page number.
     * @param limit The page size.
     * @return A paginated list of message DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedMessagesDto listChatMessages(UUID userId, UUID chatId, int page, int limit) {
        // Ensures user is a participant of the chat
        chatService.getChatForUser(chatId, userId);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Message> messagePage = messageRepository.findByChatIdOrderByCreatedAtDesc(chatId, pageable);

        List<MessageDto> messageDtos = messagePage.getContent().stream()
                .map(this::convertToMessageDto)
                .collect(Collectors.toList());

        return new PaginatedMessagesDto(messageDtos, PaginationDto.fromPage(messagePage));
    }

    /**
     * Sends a new message, creating a new chat if one doesn't exist.
     * @param senderId The ID of the user sending the message.
     * @param request The request DTO with recipient and content.
     * @return The created message DTO.
     */
    @Override
    @Transactional
    public MessageDto sendMessage(UUID senderId, SendMessageRequestDto request) {
        if (senderId.equals(request.getRecipientId())) {
            throw new BadRequestException("Cannot send a message to yourself.");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found."));
        User recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipient user not found."));

        Chat chat = chatRepository.findChatBetweenUsers(senderId, request.getRecipientId())
                .orElseGet(() -> createNewChat(sender, recipient));

        Message message = new Message();
        message.setSender(sender);
        message.setChat(chat);
        message.setContent(request.getContent());

        Message savedMessage = messageRepository.save(message);
        chat.setUpdatedAt(savedMessage.getCreatedAt()); // Touch the chat to update its timestamp
        chatRepository.save(chat);

        return convertToMessageDto(savedMessage);
    }

    private Chat createNewChat(User user1, User user2) {
        Chat chat = new Chat();
        Set<ChatParticipant> participants = new HashSet<>();
        
        ChatParticipant p1 = new ChatParticipant();
        p1.setChat(chat);
        p1.setUser(user1);
        
        ChatParticipant p2 = new ChatParticipant();
        p2.setChat(chat);
        p2.setUser(user2);
        
        participants.add(p1);
        participants.add(p2);

        chat.setParticipants(participants);
        return chatRepository.save(chat);
    }

    private MessageDto convertToMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .chatId(message.getChat().getId())
                .senderId(message.getSender().getId())
                .content(message.getContent())
                .timestamp(message.getCreatedAt())
                .build();
    }
}
```
```java
// src/main/java/com/example/service/OtpService.java