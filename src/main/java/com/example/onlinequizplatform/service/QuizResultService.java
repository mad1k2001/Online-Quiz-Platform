package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.QuizResultDto;

import java.util.List;

public interface QuizResultService {

    List<QuizResultDto> getResultsByUserEmail(String email);

    boolean isAnsweredQuiz(String email, Long id);
    QuizResultDto getQuizResults(Long quizId);
}
