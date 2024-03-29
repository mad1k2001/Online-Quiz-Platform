package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.QuestionDto;
import com.example.onlinequizplatform.dto.QuestionSolveDto;
import com.example.onlinequizplatform.dto.QuizDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface QuizService {
    List<QuizDto> getQuizzes();
    Long createQuiz(QuizDto quizDto, String email);
    void updateQuiz(QuizDto quizDto, String email, Long quizzesId);
    Long createQuestionForQuiz(Long quizId, QuestionDto questionDto);
    QuizDto getQuizById(Long quizId);
    void solve (Long quizId, List<QuestionSolveDto> questionSolveDtos, Authentication auth);
    void rateQuiz(Long quizId, int correctAnswersCount, int totalQuestionsCount);
}