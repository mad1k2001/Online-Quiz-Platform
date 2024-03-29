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
public class QuizResultDto {
    private Long id;
    private BigDecimal score;
    private Long quizId;
    private Long userId;
    private int correctAnswers;
    private int totalQuestions;
}
