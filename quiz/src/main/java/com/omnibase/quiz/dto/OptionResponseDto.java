package com.omnibase.quiz.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionResponseDto {
    private Long id;
    private Long questionId;
    private String text;
    private Boolean isCorrect; // This might be omitted for public quiz taking
}