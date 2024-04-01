package com.example.onlinequizplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
    private Long id;
    private String questionText;
    private Long quizId;
    private List<OptionDto> option;
    private int timeLimit;
}
