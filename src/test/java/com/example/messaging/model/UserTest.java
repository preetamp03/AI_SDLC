package com.example.messaging.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    /**
     * Tests basic instantiation and property setting for the User entity.
     */
    @Test
    void testUserEntity() {
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setName("Test User");
        user.setPhoneNumber("1234567890");
        user.setPassword("hashedpassword");

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo("Test User");
        assertThat(user.getPhoneNumber()).isEqualTo("1234567890");
        assertThat(user.getPassword()).isEqualTo("hashedpassword");
        assertThat(user.getConversations()).isNotNull().isEmpty();
    }
}