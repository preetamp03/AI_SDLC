package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UUID userId;
    private String phoneNumber;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        phoneNumber = "+15555555555";
        user = User.builder()
            .id(userId)
            .phoneNumber(phoneNumber)
            .name("Test User")
            .build();
    }

    @Test
    void findByPhoneNumber_whenUserExists_shouldReturnUser() {
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));
        User foundUser = userService.findByPhoneNumber(phoneNumber);
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void findByPhoneNumber_whenUserDoesNotExist_shouldThrowResourceNotFoundException() {
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.findByPhoneNumber(phoneNumber));
    }

    @Test
    void findById_whenUserExists_shouldReturnUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        User foundUser = userService.findById(userId);
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void findById_whenUserDoesNotExist_shouldThrowResourceNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(userId));
    }
}