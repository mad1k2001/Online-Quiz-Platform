package com.example.onlinequizplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsDto {
    private int totalQuizzes;
    private BigDecimal averageScore;
    private BigDecimal maxScore;
    private BigDecimal minScore;
}
