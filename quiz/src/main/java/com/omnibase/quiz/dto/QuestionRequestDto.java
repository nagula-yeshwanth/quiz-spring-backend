package com.omnibase.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionRequestDto {
    
    @NotBlank(message = "Question text is required")
    private String text;
    
    @NotBlank(message = "Question type is required")
    private String type;
}