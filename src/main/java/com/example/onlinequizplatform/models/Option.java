package com.example.onlinequizplatform.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Option {
    private Long id;
    private String optionText;
    private Boolean isCorrect;
    private Long questionId;
}
