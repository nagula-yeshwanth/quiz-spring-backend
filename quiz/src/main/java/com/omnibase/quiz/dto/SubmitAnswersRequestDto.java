package com.omnibase.quiz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SubmitAnswersRequestDto {
    
    @NotNull(message = "Attempt number is required")
    private Integer attemptNumber;
    
    private List<AnswerDto> answers;
    
    @Data
    public static class AnswerDto {
        @NotNull(message = "Question ID is required")
        private Long questionId;
        
        private Long optionId;
        private String text;
    }
}