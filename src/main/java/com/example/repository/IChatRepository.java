package com.example.repository;

import com.example.model.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IChatRepository extends JpaRepository<Chat, UUID> {

    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p.user.id = :userId")
    Page<Chat> findChatsByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE c.id = :chatId AND p.user.id = :userId")
    Optional<Chat> findByIdAndParticipant(@Param("chatId") UUID chatId, @Param("userId") UUID userId);
    
    @Query("SELECT c FROM Chat c " +
           "JOIN c.participants p1 " +
           "JOIN c.participants p2 " +
           "WHERE p1.user.id = :userId1 AND p2.user.id = :userId2 AND " +
           "(SELECT COUNT(p) FROM c.participants p) = 2")
    Optional<Chat> findChatBetweenUsers(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);
}
```
```java
// src/main/java/com/example/repository/IMessageRepository.java