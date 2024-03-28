package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.models.Question;
import com.example.onlinequizplatform.models.Option;

public interface QuestionService {
    Long createQuestion(Question question);
    Long createOption(Option option);
}