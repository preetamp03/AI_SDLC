package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.SendMessageRequestDto;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Chat;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.IChatRepository;
import com.example.repository.IMessageRepository;
import com.example.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private IMessageRepository messageRepository;
    @Mock
    private IChatRepository chatRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IChatService chatService;

    @InjectMocks
    private MessageService messageService;

    private User sender;
    private User recipient;
    private SendMessageRequestDto sendMessageRequest;
    private Chat chat;
    private Message message;
    
    @BeforeEach
    void setUp() {
        sender = User.builder().id(UUID.randomUUID()).build();
        recipient = User.builder().id(UUID.randomUUID()).build();
        
        sendMessageRequest = new SendMessageRequestDto();
        sendMessageRequest.setRecipientId(recipient.getId());
        sendMessageRequest.setContent("Hello");

        chat = new Chat();
        chat.setId(UUID.randomUUID());
        
        message = new Message();
        message.setId(UUID.randomUUID());
        message.setChat(chat);
        message.setSender(sender);
        message.setContent("Hello");
        message.setCreatedAt(Instant.now());
    }

    /**
     * Test sending a message successfully when a chat already exists.
     */
    @Test
    void sendMessage_existingChat_success() {
        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(recipient.getId())).thenReturn(Optional.of(recipient));
        when(chatRepository.findChatBetweenUsers(sender.getId(), recipient.getId())).thenReturn(Optional.of(chat));
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);

        MessageDto result = messageService.sendMessage(sender.getId(), sendMessageRequest);

        assertNotNull(result);
        assertEquals("Hello", result.getContent());
    }
    
    /**
     * Test sending a message to oneself, which should fail.
     */
    @Test
    void sendMessage_toSelf_throwsBadRequestException() {
        sendMessageRequest.setRecipientId(sender.getId());
        assertThrows(BadRequestException.class, () -> messageService.sendMessage(sender.getId(), sendMessageRequest));
    }

    /**
     * Test sending a message to a non-existent recipient.
     */
    @Test
    void sendMessage_recipientNotFound_throwsResourceNotFoundException() {
        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(recipient.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messageService.sendMessage(sender.getId(), sendMessageRequest));
    }
}
```
```java
// src/test/java/com/example/service/OtpServiceTest.java