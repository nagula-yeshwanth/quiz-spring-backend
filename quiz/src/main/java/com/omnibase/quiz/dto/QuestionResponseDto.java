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
public class QuestionResponseDto {
    private Long id;
    private Long quizId;
    private String text;
    private String type;
    private LocalDateTime createdAt;
    private List<OptionResponseDto> options;
}