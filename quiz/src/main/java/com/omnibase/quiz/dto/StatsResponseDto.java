package com.omnibase.quiz.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponseDto {
    private Long totalQuizzes;
    private Long totalUsers;
    private Long totalAttempts;
    private BigDecimal averageScore;
}