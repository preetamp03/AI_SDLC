package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.PaginationDto;
import com.example.dto.SendMessageRequestDto;
import com.example.exception.BadRequestException;
import com.example.logging.LogExecutionTime;
import com.example.model.Chat;
import com.example.model.ChatParticipant;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.IChatRepository;
import com.example.repository.IMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final IMessageRepository messageRepository;
    private final IChatRepository chatRepository;
    private final IChatService chatService;
    private final IUserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    @Transactional(readOnly = true)
    public PaginatedMessagesDto listChatMessages(UUID userId, UUID chatId, Pageable pageable) {
        // Ensure user has access to the chat
        chatService.getChatForUser(chatId, userId);

        Page<Message> messagePage = messageRepository.findByChatId(chatId, pageable);

        List<MessageDto> messageDtos = messagePage.getContent().stream()
                .map(this::convertToMessageDto)
                .collect(Collectors.toList());

        PaginationDto paginationDto = new PaginationDto(
                messagePage.getNumber() + 1,
                messagePage.getTotalPages(),
                messagePage.getSize(),
                messagePage.getTotalElements(),
                messagePage.hasNext(),
                messagePage.hasPrevious()
        );

        return new PaginatedMessagesDto(messageDtos, paginationDto);
    }
    
    /**
     * Converts a Message entity to a MessageDto.
     * @param message The Message entity.
     * @return A MessageDto.
     */
    private MessageDto convertToMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .chatId(message.getChat().getId())
                .senderId(message.getSender().getId())
                .content(message.getContent())
                .timestamp(message.getCreatedAt())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @LogExecutionTime
    @Transactional
    public MessageDto sendMessage(UUID senderId, SendMessageRequestDto requestDto) {
        if (senderId.equals(requestDto.getRecipientId())) {
            throw new BadRequestException("Cannot send a message to yourself.");
        }
        
        User sender = userService.findById(senderId);
        User recipient = userService.findById(requestDto.getRecipientId());

        Chat chat = findOrCreateChat(sender, recipient);

        Message message = new Message();
        message.setSender(sender);
        message.setChat(chat);
        message.setContent(requestDto.getContent());
        message.setCreatedAt(Instant.now()); // Set here to ensure consistency
        
        chat.setUpdatedAt(message.getCreatedAt()); // Touch the chat to update its timestamp
        chatRepository.save(chat);
        
        Message savedMessage = messageRepository.save(message);
        
        return convertToMessageDto(savedMessage);
    }

    /**
     * Finds an existing chat between two users or creates a new one.
     * @param user1 The first user.
     * @param user2 The second user.
     * @return The existing or newly created Chat.
     */
    private Chat findOrCreateChat(User user1, User user2) {
        return chatRepository.findChatBetweenUsers(user1.getId(), user2.getId())
                .orElseGet(() -> {
                    Chat newChat = new Chat();
                    newChat = chatRepository.save(newChat); // Save to get an ID
                    
                    ChatParticipant participant1 = new ChatParticipant(newChat, user1);
                    ChatParticipant participant2 = new ChatParticipant(newChat, user2);
                    
                    newChat.getParticipants().add(participant1);
                    newChat.getParticipants().add(participant2);
                    
                    return chatRepository.save(newChat);
                });
    }
}