package com.example.onlinequizplatform.models;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Question {
    private Long id;
    private String questionText;
    private Long quizId;
    private int timeLimit;
}
