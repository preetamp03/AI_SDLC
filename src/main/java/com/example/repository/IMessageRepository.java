package com.example.repository;

import com.example.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IMessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findByChatId(UUID chatId, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Message m " +
           "WHERE m.chat.id = :chatId AND m.id NOT IN " +
           "(SELECT mrs.id.messageId FROM MessageReadStatus mrs WHERE mrs.id.userId = :userId)")
    long countUnreadMessagesForUserInChat(@Param("chatId") UUID chatId, @Param("userId") UUID userId);
}
```
```java
// src/main/java/com/example/repository/IOtpRepository.java