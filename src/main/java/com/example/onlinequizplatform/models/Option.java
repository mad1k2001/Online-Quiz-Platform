package com.example.onlinequizplatform.models;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Option {
    private Long id;
    private String optionText;
    private Boolean isCorrect;
    private Long questionId;
}
