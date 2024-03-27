package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.CreateQuizDto;

public interface QuizService {
    Long createQuiz(CreateQuizDto quizDto, Long creatorId);
}
