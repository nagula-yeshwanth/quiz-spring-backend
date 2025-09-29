package com.omnibase.quiz.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponseDto {
    private Long id;
    private String title;
    private String description;
    private Integer maxAttempts;
    private Long createdBy;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<QuestionResponseDto> questions;
}