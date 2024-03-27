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
public class CreateQuizDto {
    private String title;
    private String description;
    private Long creatorId;
    private List<QuestionDto> question;
    private List<OptionDto> option;
}
