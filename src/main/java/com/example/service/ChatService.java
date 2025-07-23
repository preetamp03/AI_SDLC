package com.example.service;

import com.example.dto.ChatSummaryDto;
import com.example.dto.MessageSummaryDto;
import com.example.dto.PaginatedChatsDto;
import com.example.dto.UserSummaryDto;
import com.example.exception.ForbiddenException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Chat;
import com.example.model.Message;
import com.example.model.User;
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
public class ChatService implements IChatService {

    private final ChatRepository chatRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final MessageRepository messageRepository;

    /**
     * Retrieves a paginated list of chats for a given user.
     * @param userId The ID of the user.
     * @param pageable Pagination and sorting options.
     * @param search A search term for the recipient's name.
     * @return A paginated DTO of chat summaries.
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedChatsDto listUserChats(UUID userId, Pageable pageable, String search) {
        Page<Chat> chatPage = chatRepository.findChatsByUserId(userId, search, pageable);
        List<ChatSummaryDto> chatSummaries = chatPage.getContent().stream()
                .map(chat -> convertToChatSummaryDto(chat, userId))
                .collect(Collectors.toList());

        return new PaginatedChatsDto(chatSummaries, PaginationUtil.createPaginationDto(chatPage));
    }

    /**
     * Marks all messages in a chat as read for a specific user.
     * This is a placeholder; a real implementation would update MessageReadStatus.
     * @param userId The ID of the user.
     * @param chatId The ID of the chat.
     */
    @Override
    @Transactional
    public void markChatAsRead(UUID userId, UUID chatId) {
        getChatForUser(chatId, userId); // Ensures user is a participant
        // In a real application, you would iterate through unread messages
        // for this user in this chat and create MessageReadStatus entries.
        // For this example, we'll just log it.
        System.out.println("Marking chat " + chatId + " as read for user " + userId);
    }

    /**
     * Retrieves a chat by its ID, ensuring the user is a participant.
     * @param chatId The ID of the chat.
     * @param userId The ID of the user.
     * @return The Chat entity.
     * @throws ResourceNotFoundException if the chat doesn't exist.
     * @throws ForbiddenException if the user is not a participant.
     */
    @Override
    public Chat getChatForUser(UUID chatId, UUID userId) {
        chatRepository.findById(chatId)
            .orElseThrow(() -> new ResourceNotFoundException("Chat not found."));

        if (!chatParticipantRepository.existsByChatIdAndUserId(chatId, userId)) {
            throw new ForbiddenException("You do not have permission to access this chat.");
        }
        return chatRepository.findById(chatId).get(); // Safe get after check
    }

    private ChatSummaryDto convertToChatSummaryDto(Chat chat, UUID currentUserId) {
        User recipient = chat.getParticipants().stream()
                .map(p -> p.getUser())
                .filter(u -> !u.getId().equals(currentUserId))
                .findFirst()
                .orElse(null); // Should not happen in a 2-person chat

        UserSummaryDto recipientDto = (recipient != null) ?
                UserSummaryDto.builder()
                        .id(recipient.getId())
                        .name(recipient.getName())
                        .avatarUrl(recipient.getAvatarUrl())
                        .build() : null;

        MessageSummaryDto lastMessageDto = messageRepository.findTopByChatIdOrderByCreatedAtDesc(chat.getId())
                .map(this::convertToMessageSummaryDto)
                .orElse(null);
        
        long unreadCount = messageRepository.countUnreadMessages(chat.getId(), currentUserId);

        return ChatSummaryDto.builder()
                .id(chat.getId())
                .recipient(recipientDto)
                .lastMessage(lastMessageDto)
                .unreadCount(unreadCount)
                .isRead(unreadCount == 0)
                .build();
    }

    private MessageSummaryDto convertToMessageSummaryDto(Message message) {
        return MessageSummaryDto.builder()
                .content(message.getContent())
                .timestamp(message.getCreatedAt())
                .build();
    }
}