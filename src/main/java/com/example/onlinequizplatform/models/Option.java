package com.example.onlinequizplatform.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class Option {
    private Long id;
    private String optionText;
    private Boolean isCorrect;
    private Long questionId;
}
