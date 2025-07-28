package com.example.messaging.service;

import com.example.messaging.dto.MessageDto;
import com.example.messaging.dto.SendMessageRequest;
import com.example.messaging.model.Conversation;
import com.example.messaging.model.Message;
import com.example.messaging.model.User;
import com.example.messaging.repository.MessageRepository;
import com.example.messaging.repository.UserRepository;
import com.example.messaging.util.EventLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private IConversationService conversationService;
    @Mock
    private EventLogger eventLogger;

    @InjectMocks
    private MessageService messageService;

    /**
     * Tests the successful creation of a message.
     */
    @Test
    void createMessage_shouldSucceed() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        SendMessageRequest request = new SendMessageRequest(recipientId, "Hello");

        User sender = new User();
        sender.setId(senderId);
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setSender(sender);
        message.setConversation(conversation);
        message.setContent("Hello");
        message.setCreatedAt(OffsetDateTime.now());

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.existsById(recipientId)).thenReturn(true);
        when(conversationService.findOrCreateConversation(senderId, recipientId)).thenReturn(conversation);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        MessageDto result = messageService.createMessage(senderId, request);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("Hello");
        assertThat(result.getSenderId()).isEqualTo(senderId);
    }
}