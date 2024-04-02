package com.example.onlinequizplatform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSolveDto {
    @NotBlank(message = "field not filled in questionText")
    private String questionText;
    @NotBlank(message = "field not filled in answers")
    private String answers;
}
