package com.example.onlinequizplatform.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class QuizResult {
    private Long id;
    private BigDecimal score;
    private Long quizId;
    private Long userId;
}
