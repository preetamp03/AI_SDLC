package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.PaginatedMessagesDto;
import com.example.dto.SendMessageRequest;
import com.example.exception.BadRequestException;
import com.example.model.Chat;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.ChatRepository;
import com.example.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private IUserService userService;
    @Mock
    private IChatService chatService;

    @InjectMocks
    private MessageService messageService;

    private User sender;
    private User recipient;
    private Chat chat;
    private UUID chatId;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        sender = User.builder().id(UUID.randomUUID()).build();
        recipient = User.builder().id(UUID.randomUUID()).build();
        chatId = UUID.randomUUID();
        chat = new Chat();
        chat.setId(chatId);
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void listChatMessages_shouldReturnPaginatedMessages() {
        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setChat(chat);
        message.setSender(sender);
        message.setContent("Hello");
        Page<Message> messagePage = new PageImpl<>(Collections.singletonList(message));

        when(chatService.getChatForUser(chatId, sender.getId())).thenReturn(chat);
        when(messageRepository.findByChatId(chatId, pageable)).thenReturn(messagePage);

        PaginatedMessagesDto result = messageService.listChatMessages(sender.getId(), chatId, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getData()).hasSize(1);
        assertThat(result.getData().get(0).getContent()).isEqualTo("Hello");
        assertThat(result.getPagination().getTotalCount()).isEqualTo(1);
    }

    @Test
    void sendMessage_toSelf_shouldThrowBadRequestException() {
        SendMessageRequest request = new SendMessageRequest();
        request.setRecipientId(sender.getId());
        request.setContent("Hi me");

        assertThrows(BadRequestException.class, () -> messageService.sendMessage(sender.getId(), request));
    }

    @Test
    void sendMessage_toExistingChat_shouldSaveMessage() {
        SendMessageRequest request = new SendMessageRequest();
        request.setRecipientId(recipient.getId());
        request.setContent("Hi you");

        when(userService.findById(sender.getId())).thenReturn(sender);
        when(userService.findById(recipient.getId())).thenReturn(recipient);
        when(chatRepository.findChatBetweenUsers(sender.getId(), recipient.getId())).thenReturn(Optional.of(chat));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message msg = invocation.getArgument(0);
            msg.setId(UUID.randomUUID());
            return msg;
        });

        MessageDto result = messageService.sendMessage(sender.getId(), request);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("Hi you");
        verify(chatRepository, never()).save(any(Chat.class)); // Should not create a new chat
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void sendMessage_toNewChat_shouldCreateChatAndSaveMessage() {
        SendMessageRequest request = new SendMessageRequest();
        request.setRecipientId(recipient.getId());
        request.setContent("Hi you, first time?");

        when(userService.findById(sender.getId())).thenReturn(sender);
        when(userService.findById(recipient.getId())).thenReturn(recipient);
        when(chatRepository.findChatBetweenUsers(sender.getId(), recipient.getId())).thenReturn(Optional.empty());
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message msg = invocation.getArgument(0);
            msg.setId(UUID.randomUUID());
            return msg;
        });

        MessageDto result = messageService.sendMessage(sender.getId(), request);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("Hi you, first time?");
        verify(chatRepository, times(2)).save(any(Chat.class)); // Once for creation, once for update timestamp
        verify(messageRepository).save(any(Message.class));
    }
}