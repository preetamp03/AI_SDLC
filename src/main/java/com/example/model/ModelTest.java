package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

/**
 * Basic tests for model (entity) classes.
 */
class ModelTest {
    
    @Test
    void testUser() {
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        assertEquals(id, user.getId());
    }
    
    @Test
    void testChat() {
        Chat chat = new Chat();
        UUID id = UUID.randomUUID();
        chat.setId(id);
        assertEquals(id, chat.getId());
    }
    
    @Test
    void testMessage() {
        Message message = new Message();
        message.setContent("Hello");
        assertEquals("Hello", message.getContent());
    }

    // Add similar simple tests for other models
}
```
```java
// src/test/java/com/example/repository/RepositoryTest.java