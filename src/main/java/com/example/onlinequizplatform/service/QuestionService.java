package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.models.Question;
import com.example.onlinequizplatform.models.Option;

import java.util.List;

public interface QuestionService {
    Long createQuestion(Question question);
    Long createOption(Option option);
    void updateQuestion(Question question);
}