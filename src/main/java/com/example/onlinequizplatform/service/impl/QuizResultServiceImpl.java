package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.QuizResultDao;
import com.example.onlinequizplatform.dto.QuizResultDto;
import com.example.onlinequizplatform.dto.TopPlayersDto;
import com.example.onlinequizplatform.dto.UserDto;
import com.example.onlinequizplatform.exeptions.CustomException;
import com.example.onlinequizplatform.models.Question;
import com.example.onlinequizplatform.models.QuizResult;
import com.example.onlinequizplatform.models.TopPlayers;
import com.example.onlinequizplatform.service.QuizResultService;
import com.example.onlinequizplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<QuizResultDto> getQuizLeaderboard(Long quizId) {
        if (quizId == null) {
            log.error("Invalid input: quizId is null");
            throw new CustomException("quizId cannot be null");
        }

        List<QuizResult> quizResults = quizResultDao.getQuizResultsByQuizId(quizId);
        if (quizResults == null || quizResults.isEmpty()) {
            log.warn("No quiz results found for quizId={}", quizId);
            return Collections.emptyList();
        }

        List<QuizResult> sortedResults = quizResults.stream()
                .sorted(Comparator.comparing(QuizResult::getScore).reversed())
                .collect(Collectors.toList());

        log.debug("Retrieved and sorted quiz results for quizId={}, totalResults={}", quizId, sortedResults.size());

        return mapByQuizResultDto(sortedResults);
    }

    @Override
    public List<TopPlayersDto> topFivePlayers() {
        return mapTopPlayersDto(quizResultDao.topFivePlayers());
    }

    @Override
    public List<TopPlayersDto> topTenPlayers() {
        return mapTopPlayersDto(quizResultDao.topTenPlayers());
    }

    public List<TopPlayersDto> mapTopPlayersDto(List<TopPlayers> topPlayers ){
        List<TopPlayersDto> topPlayersDtos = new ArrayList<>();
        topPlayers.forEach(e ->
                        topPlayersDtos.add( TopPlayersDto.builder()
                                        .score(e.getScore())
                                        .userName(e.getUserName())
                                        .userId(e.getUserId())
                                        .position(e.getPosition())
                                .build())
                );
        return topPlayersDtos;
    }

    public Long createQuizResult(BigDecimal score, Long quizId, Long userId, int correctAnswers, int totalQuestions){
        QuizResult quiz= new QuizResult();
        quiz.setScore(score);
        quiz.setQuizId(quizId);
        quiz.setUserId(userId);
        quiz.setCorrectAnswers(correctAnswers);
        quiz.setTotalQuestions(totalQuestions);
        Long id=quizResultDao.createQuizResult(quiz);

        quizResultDao.clearBestPlayersTable();
        quizResultDao.insertTenBestPlayers();
        return id;
    }

    private QuizResultDto mapToDto(QuizResult quizResult) {
        Integer correctAnswers = quizResult.getCorrectAnswers();
        int correctAnswersValue = correctAnswers != null ? correctAnswers.intValue() : 0;

        Integer totalQuestions = quizResult.getTotalQuestions();
        int totalQuestionsValue = totalQuestions != null ? totalQuestions.intValue() : 0;

        return QuizResultDto.builder()
                .id(quizResult.getId())
                .quizId(quizResult.getQuizId())
                .score(quizResult.getScore())
                .userId(quizResult.getUserId())
                .correctAnswers(correctAnswersValue)
                .totalQuestions(totalQuestionsValue)
                .build();
    }

    public List<QuizResultDto> mapByQuizResultDto(List<QuizResult> quizResults) {
        List<QuizResultDto> quizResultDtos = new ArrayList<>();
        quizResults.forEach(e -> quizResultDtos.add(mapToDto(e)));
        return quizResultDtos;
    }


}

