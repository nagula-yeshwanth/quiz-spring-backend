package com.omnibase.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class QuizRequestDto {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @Min(value = 1, message = "Max attempts must be at least 1")
    private Integer maxAttempts = 1;
    
    private Boolean isActive = true;
}