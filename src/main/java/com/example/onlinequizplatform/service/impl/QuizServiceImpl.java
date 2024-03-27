package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.QuestionDao;
import com.example.onlinequizplatform.dao.QuizDao;
import com.example.onlinequizplatform.dao.UserDao;
import com.example.onlinequizplatform.dto.QuizDto;
import com.example.onlinequizplatform.models.Quiz;
import com.example.onlinequizplatform.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final UserDao userDao;
    private final QuizDao quizDao;
    private final QuestionDao questionDao;

    @Override
    public List<QuizDto> getQuizzes() {
        List<Quiz> quizzes = quizDao.getQuizzes();
        List<QuizDto> dto = new ArrayList<>();
        quizzes.forEach(e -> dto.add(makeQuizDto(e)));
        return dto;
    }

    @Override
    public Long createQuiz(QuizDto quizDto, String email) {
        Quiz quiz = makeQuiz(quizDto);
        return quizDao.createQuiz(quiz);
    }

    @Override
    public void updateQuiz(QuizDto quizDto, String email, Long quizzesId){
        Quiz quiz = makeQuiz(quizDto);
        quizDao.updateQuiz(quiz);
    }

    private Quiz makeQuiz(QuizDto quizDto){
        return Quiz.builder()
                .title(quizDto.getTitle())
                .description(quizDto.getDescription())
                .creatorId(quizDto.getCreatorId())
                .build();
    }

    private QuizDto makeQuizDto(Quiz quiz){
        return QuizDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .creatorId(quiz.getCreatorId())
                .build();
    }

//    private List<QuizDto> getQuizDto(List<Quiz> foundResumes) {
//        List<QuizDto> dto = new ArrayList<>();
//        for (Quiz quiz : foundResumes) {
//            QuizDto quizDto = QuizDto.builder()
//                    .id(quiz.getId())
//                    .title(quiz.getTitle())
//                    .description(quiz.getDescription())
//                    .creatorId(quiz.getCreatorId())
//                    .build();
//
//            List<QuestionDto> questionDtoList = questionDao.getContactInfoByResumeId(quiz.getId()).stream()
//                    .map(this::mapToContactInfoDto)
//                    .collect(Collectors.toList());
//
//            resumeDto.setContactInfo(contactInfoList);
//            dto.add(quizDto);
//        }
//        return dto;
//    }
}
