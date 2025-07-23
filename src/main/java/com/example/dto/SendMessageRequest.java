package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class SendMessageRequest {
    @NotNull(message = "Recipient ID is required")
    private UUID recipientId;

    @NotBlank(message = "Message content cannot be empty")
    @Size(min = 1, max = 5000, message = "Content must be between 1 and 5000 characters")
    private String content;
}