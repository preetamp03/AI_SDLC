package com.example.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatParticipantId implements Serializable {
    private UUID chatId;
    private UUID userId;
}
```
```java
// src/main/java/com/example/model/ChatParticipant.java