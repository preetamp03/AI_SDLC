package com.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserSummaryDto {
    private UUID id;
    private String name;
    private String avatarUrl;
}