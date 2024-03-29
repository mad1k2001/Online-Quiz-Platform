package com.example.onlinequizplatform.models;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizResult {
    private Long id;
    private BigDecimal score;
    private Long quizId;
    private Long userId;
    private int correctAnswers;
    private int totalQuestions;
}
