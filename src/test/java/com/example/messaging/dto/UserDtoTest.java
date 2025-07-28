package com.example.messaging.dto;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    /**
     * Tests the builder and getters of the UserDto.
     */
    @Test
    void testUserDto() {
        UUID id = UUID.randomUUID();
        UserDto dto = UserDto.builder()
                .id(id)
                .name("John Doe")
                .phoneNumber("1234567890")
                .build();

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo("John Doe");
        assertThat(dto.getPhoneNumber()).isEqualTo("1234567890");
    }
}