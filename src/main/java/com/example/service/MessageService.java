package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.SendMessageRequest;
import com.example.exception.BadRequestException;
import com.example.model.*;
import com.example.repository.ChatParticipantRepository;
import com.example.repository.ChatRepository;
import com.example.repository.MessageRepository;
import com.example.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final IUserService userService;
    private final IChatService chatService;

    /**
     * Retrieves a paginated list of messages for a given chat, ensuring the user is a participant.
     * @param userId The ID of the user making the request.
     * @param chatId The ID of the chat.
     * @param pageable Pagination options.
     * @return A paginated DTO of messages.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedMessagesDto listChatMessages(UUID userId, UUID chatId, Pageable pageable) {
        chatService.getChatForUser(chatId, userId); // Throws if user not in chat
        Page<Message> messagePage = messageRepository.findByChatId(chatId, pageable);
        List<MessageDto> messageDtos = messagePage.getContent().stream()
                .map(this::convertToMessageDto)
                .collect(Collectors.toList());

        return new PaginatedMessagesDto(messageDtos, PaginationUtil.createPaginationDto(messagePage));
    }

    /**
     * Sends a message to a recipient, creating a new chat if one doesn't exist.
     * @param senderId The ID of the user sending the message.
     * @param request The request containing recipient ID and content.
     * @return The DTO of the newly created message.
     */
    @Override
    @Transactional
    public MessageDto sendMessage(UUID senderId, SendMessageRequest request) {
        if (senderId.equals(request.getRecipientId())) {
            throw new BadRequestException("Cannot send a message to yourself.");
        }
        User sender = userService.findById(senderId);
        User recipient = userService.findById(request.getRecipientId());

        Chat chat = chatRepository.findChatBetweenUsers(senderId, recipient.getId())
                .orElseGet(() -> createNewChat(sender, recipient));

        Message message = new Message();
        message.setSender(sender);
        message.setChat(chat);
        message.setContent(request.getContent());

        Message savedMessage = messageRepository.save(message);
        chat.setUpdatedAt(savedMessage.getCreatedAt());
        chatRepository.save(chat);

        return convertToMessageDto(savedMessage);
    }

    private Chat createNewChat(User user1, User user2) {
        Chat chat = new Chat();
        Chat savedChat = chatRepository.save(chat);

        ChatParticipant participant1 = new ChatParticipant();
        participant1.setChat(savedChat);
        participant1.setUser(user1);

        ChatParticipant participant2 = new ChatParticipant();
        participant2.setChat(savedChat);
        participant2.setUser(user2);

        chatParticipantRepository.save(participant1);
        chatParticipantRepository.save(participant2);

        return savedChat;
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