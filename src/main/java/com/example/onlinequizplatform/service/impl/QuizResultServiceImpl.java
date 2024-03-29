package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.QuizResultDao;
import com.example.onlinequizplatform.dto.OptionDto;
import com.example.onlinequizplatform.dto.QuestionDto;
import com.example.onlinequizplatform.dto.QuizResultDto;
import com.example.onlinequizplatform.models.QuizResult;
import com.example.onlinequizplatform.service.QuizResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizResultServiceImpl implements QuizResultService {
    private final QuizResultDao quizResultDao;

    @Override
    public List<QuizResultDto> getResultsByUserEmail(String email){
        return mapByQuizResultDto(quizResultDao.getResultsByUserEmail(email));
    }

    @Override
    public boolean isAnsweredQuiz(String email, Long id){
        return quizResultDao.isAnswered(email, id);

    }

    @Override
    public QuizResultDto getQuizResults(Long quizId) {
        List<QuestionDto> questions = quizResultDao.getQuestionsByQuizId(quizId);
        List<OptionDto> userAnswers = quizResultDao.getUserAnswersForQuiz(quizId);

        QuizResultDto quizResultDto = calculateQuizResult(quizId, questions, userAnswers);

        return quizResultDto;
    }

    private QuizResultDto calculateQuizResult(Long quizId, List<QuestionDto> questions, List<OptionDto> userAnswers) {
        int correctAnswersCount = 0;
        int totalQuestionsCount = questions.size();

        for (QuestionDto question : questions) {
            Long questionId = question.getId();
            OptionDto userAnswer = getUserAnswerByQuestionId(userAnswers, questionId);
            if (userAnswer != null && userAnswer.getIsCorrect()) {
                correctAnswersCount++;
            }
        }

        double score = ((double) correctAnswersCount / totalQuestionsCount) * 100;

        return QuizResultDto.builder()
                .quizId(quizId)
                .score(BigDecimal.valueOf(score))
                .build();
    }

    private OptionDto getUserAnswerByQuestionId(List<OptionDto> userAnswers, Long questionId) {
        for (OptionDto answer : userAnswers) {
            if (answer.getQuestionId().equals(questionId)) {
                return answer;
            }
        }
        return null;
    }


    private QuizResultDto mapToDto(QuizResult quizResult) {
        return QuizResultDto.builder()
                .id(quizResult.getId())
                .quizId(quizResult.getQuizId())
                .score(quizResult.getScore())
                .userId(quizResult.getUserId())
                .build();
    }

    public List<QuizResultDto> mapByQuizResultDto(List<QuizResult> quizResults) {
        List<QuizResultDto> quizResultDtos = new ArrayList<>();
        quizResults.forEach(e -> quizResultDtos.add(mapToDto(e)));
        return quizResultDtos;
    }
}
