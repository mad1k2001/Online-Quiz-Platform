package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.CreateQuizDto;
import com.example.onlinequizplatform.dto.QuizDto;
import com.example.onlinequizplatform.models.Quiz;

import java.util.List;

public interface QuizService {
    List<QuizDto> getQuizzes();
    Long createQuiz(CreateQuizDto quizDto, String email);
}
