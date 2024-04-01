package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.QuizResultDto;

import java.math.BigDecimal;
import java.util.List;

public interface QuizResultService {

    List<QuizResultDto> getResultsByUserEmail(String email);

    boolean isAnsweredQuiz(String email, Long id);
    QuizResultDto getQuizResultById(Long resultId);

    void updateQuizRating(Long quizId, Double rating);
    Long  createQuizResult(BigDecimal score, Long quizId, Long userId, int correctAnswers, int totalQuestions);
}
