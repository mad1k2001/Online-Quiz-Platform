package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.QuestionDto;
import com.example.onlinequizplatform.dto.QuizDto;

import java.util.List;

public interface QuizService {
    List<QuizDto> getQuizzes();
    Long createQuiz(QuizDto quizDto, String email);
    void updateQuiz(QuizDto quizDto, String email, Long quizzesId);
    Long createQuestionForQuiz(Long quizId, QuestionDto questionDto);
    QuizDto getQuizById(Long quizId);
}