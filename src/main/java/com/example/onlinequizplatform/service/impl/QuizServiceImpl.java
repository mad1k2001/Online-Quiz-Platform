package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.QuestionDao;
import com.example.onlinequizplatform.dao.QuizDao;
import com.example.onlinequizplatform.dao.UserDao;
import com.example.onlinequizplatform.dto.CreateQuizDto;
import com.example.onlinequizplatform.dto.QuizDto;
import com.example.onlinequizplatform.models.Quiz;
import com.example.onlinequizplatform.models.User;
import com.example.onlinequizplatform.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final UserDao userDao;
    private final QuizDao quizDao;
//    private final QuestionDao questionDao;

    @Override
    public Long createQuiz(CreateQuizDto quizDto, Long creatorId) {
//        Optional<User> userOptional = userDao.getUserById(creatorId);
//        if (userOptional.isEmpty()) {
//            log.error("Can't find user with id: " + creatorId);
//        }
        Quiz quiz = makeQuiz(quizDto);
        return quizDao.createQuiz(quiz);
    }

    private Quiz makeQuiz(CreateQuizDto quizDto){
        return Quiz.builder()
                .title(quizDto.getTitle())
                .description(quizDto.getDescription())
                .creatorId(quizDto.getCreatorId())
                .build();
    }

}
