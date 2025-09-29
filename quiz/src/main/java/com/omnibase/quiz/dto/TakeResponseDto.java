package com.omnibase.quiz.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TakeResponseDto {
    private Long id;
    private Long userId;
    private Long quizId;
    private Integer attemptNumber;
    private BigDecimal score;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private List<ResponseDto> responses;
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto {
        private Long id;
        private Long questionId;
        private Long optionId;
        private String text;
        private Boolean isCorrect;
    }
}