package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.QuizResultDto;
import com.example.onlinequizplatform.dto.TopPlayersDto;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;

public interface QuizResultService {
    List<QuizResultDto> getResultsByUserEmail(String email);
    boolean isAnsweredQuiz(String email, Long id);
    void quizRating(Long quizId, Double rating, Authentication auth);
    Long  createQuizResult(BigDecimal score, Long quizId, Long userId, int correctAnswers, int totalQuestions);
    List<QuizResultDto> getQuizLeaderboard(Long quizId);
    List<TopPlayersDto> topFivePlayers();
    List<TopPlayersDto> topTenPlayers();
    List<QuizResultDto> getQuizResultsWithPagination(Long quizId, int page, int size);
}
