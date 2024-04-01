package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.OptionDao;
import com.example.onlinequizplatform.dao.QuestionDao;
import com.example.onlinequizplatform.dao.QuizDao;
import com.example.onlinequizplatform.dao.QuizResultDao;
import com.example.onlinequizplatform.dto.QuizResultDto;
import com.example.onlinequizplatform.dto.UserDto;
import com.example.onlinequizplatform.exeptions.CustomException;
import com.example.onlinequizplatform.models.Question;
import com.example.onlinequizplatform.models.Quiz;
import com.example.onlinequizplatform.models.QuizResult;
import com.example.onlinequizplatform.service.QuizResultService;
import java.math.BigDecimal;

import com.example.onlinequizplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import com.example.onlinequizplatform.models.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizResultServiceImpl implements QuizResultService {
    private final QuizResultDao quizResultDao;
    private final UserService userService;


    @Override
    public List<QuizResultDto> getResultsByUserEmail(String email){
        return mapByQuizResultDto(quizResultDao.getResultsByUserEmail(email));
    }

    @Override
    public boolean isAnsweredQuiz(String email, Long id){
        return quizResultDao.isAnswered(email, id);
    }

    private QuizResultDto mapToDto(QuizResult quizResult) {
        return QuizResultDto.builder()
                .id(quizResult.getId())
                .quizId(quizResult.getQuizId())
                .score(quizResult.getScore())
                .userId(quizResult.getUserId())
                .correctAnswers(quizResult.getCorrectAnswers())
                .totalQuestions(quizResult.getTotalQuestions())
                .build();
    }

    public List<QuizResultDto> mapByQuizResultDto(List<QuizResult> quizResults) {
        List<QuizResultDto> quizResultDtos = new ArrayList<>();
        quizResults.forEach(e -> quizResultDtos.add(mapToDto(e)));
        return quizResultDtos;
    }

    @Override
    public QuizResultDto getQuizResultById(Long resultId, Authentication auth) {
        User user = (User) auth.getPrincipal();

        UserDto currentUser=userService.getUserByEmail(user.getUsername());

        Optional<QuizResult> quizResult = quizResultDao.getQuizResultById(resultId, currentUser.getId());
        if (quizResult.isEmpty()) {
            String message="You haven't answered this quiz";
            log.error(message);
            throw new CustomException(message);
        }
        return mapToDto(quizResult.get());
    }

    @Override
    public void quizRating(Long quizId, Double rating, Authentication auth) {
        User user = (User) auth.getPrincipal();

        UserDto currentUser=userService.getUserByEmail(user.getUsername());
        Optional<QuizResult> quizResult = quizResultDao.getQuizResultById(quizId, currentUser.getId());
        if (quizResult.isEmpty()) {
            String message="You haven't answered this quiz";
            log.error(message);
            throw new CustomException(message);
        }

        if(quizResult.get().getQuizRating()!=null){
            String message="You have already rated this quiz";
            log.error(message);
            throw new CustomException(message);
        }

        if(rating>5 || rating<1){
            String message="The rate should be within 1 - 5";
            log.error(message);
            throw new CustomException(message);
        }

        quizResultDao.updateQuizRating(quizId, rating, currentUser.getId());
    }

    public Long createQuizResult(BigDecimal score, Long quizId, Long userId, int correctAnswers, int totalQuestions){

        QuizResult quiz= new QuizResult();
        quiz.setScore(score);
        quiz.setQuizId(quizId);
        quiz.setUserId(userId);
        quiz.setCorrectAnswers(correctAnswers);
        quiz.setTotalQuestions(totalQuestions);

        return quizResultDao.createQuizResult(quiz);
    }
}

