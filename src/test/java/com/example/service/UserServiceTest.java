package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import com.example.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findByPhoneNumber_shouldReturnUser() {
        String phone = "+12345";
        User user = new User();
        when(userRepository.findByPhoneNumber(phone)).thenReturn(Optional.of(user));
        
        Optional<User> result = userService.findByPhoneNumber(phone);
        
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findById_shouldReturnUser_whenFound() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        
        User result = userService.findById(id);
        
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(id));
    }
}