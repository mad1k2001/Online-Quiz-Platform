package com.example.onlinequizplatform.models;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class QuizResult {
    private Long id;
    private BigDecimal score;
    private Long quizId;
    private Long userId;
    private Integer correctAnswers;
    private Integer totalQuestions;
    private Integer quizRating;
}
