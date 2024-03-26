package com.example.onlinequizplatform.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question {
    private Long id;
    private String questionText;
    private Long quizId;
}
