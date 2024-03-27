package com.example.onlinequizplatform.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@Builder
public class QuizResult {
    private Long id;
    private BigDecimal score;
    private Long quizId;
    private Long userId;
}
