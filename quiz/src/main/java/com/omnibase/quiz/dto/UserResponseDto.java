package com.omnibase.quiz.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}