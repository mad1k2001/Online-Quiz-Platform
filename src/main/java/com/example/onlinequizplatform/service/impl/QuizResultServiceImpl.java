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
