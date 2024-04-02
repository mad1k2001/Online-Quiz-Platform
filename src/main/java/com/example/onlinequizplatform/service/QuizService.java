package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.QuestionDto;
import com.example.onlinequizplatform.dto.QuestionSolveDto;
import com.example.onlinequizplatform.dto.QuizDto;
import com.example.onlinequizplatform.dto.QuizResultAnsverDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface QuizService {
    List<QuizDto> getQuizzes();
    Long createQuiz(QuizDto quizDto, Authentication authentication);
    void updateQuiz(QuizDto quizDto, Authentication authentication, Long quizId);
    QuizDto getQuizById(Long quizId);
    QuizResultAnsverDto solve(Long quizId, List<QuestionSolveDto> questionSolveDtos, Authentication auth);
    List<QuestionDto> getQuestionsByQuizIdWithPagination(Long quizId, int page, int size);

}