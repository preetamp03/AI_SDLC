package com.example.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChatParticipantId implements Serializable {
    private UUID chat;
    private UUID user;
}
```
```java
// src/main/java/com/example/model/Message.java