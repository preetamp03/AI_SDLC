package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.SendMessageRequestDto;
import com.example.exception.BadRequestException;
import com.example.model.Chat;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.IChatRepository;
import com.example.repository.IMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock private IMessageRepository messageRepository;
    @Mock private IChatRepository chatRepository;
    @Mock private IChatService chatService;
    @Mock private IUserService userService;
    @InjectMocks private MessageService messageService;

    @Test
    void listChatMessages_shouldReturnMessages() {
        UUID userId = UUID.randomUUID();
        UUID chatId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        
        Chat chat = new Chat();
        chat.setId(chatId);
        Message message = new Message();
        message.setChat(chat);
        message.setSender(new User());

        Page<Message> messagePage = new PageImpl<>(List.of(message));

        when(chatService.getChatForUser(chatId, userId)).thenReturn(chat); // Mocks authorization check
        when(messageRepository.findByChatId(chatId, pageable)).thenReturn(messagePage);

        PaginatedMessagesDto result = messageService.listChatMessages(userId, chatId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
    }

    @Test
    void sendMessage_shouldCreateNewMessage() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        
        SendMessageRequestDto requestDto = new SendMessageRequestDto();
        requestDto.setRecipientId(recipientId);
        requestDto.setContent("Hello");
        
        User sender = new User();
        sender.setId(senderId);
        User recipient = new User();
        recipient.setId(recipientId);

        Chat chat = new Chat();
        chat.setId(UUID.randomUUID());

        when(userService.findById(senderId)).thenReturn(sender);
        when(userService.findById(recipientId)).thenReturn(recipient);
        when(chatRepository.findChatBetweenUsers(senderId, recipientId)).thenReturn(Optional.of(chat));
        when(messageRepository.save(any(Message.class))).thenAnswer(i -> i.getArgument(0));
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);

        MessageDto result = messageService.sendMessage(senderId, requestDto);

        assertNotNull(result);
        assertEquals("Hello", result.getContent());
        assertEquals(senderId, result.getSenderId());
    }
    
    @Test
    void sendMessage_toSelf_shouldThrowException() {
        UUID senderId = UUID.randomUUID();
        
        SendMessageRequestDto requestDto = new SendMessageRequestDto();
        requestDto.setRecipientId(senderId);
        requestDto.setContent("Hi me");

        assertThrows(BadRequestException.class, () -> messageService.sendMessage(senderId, requestDto));
    }
}