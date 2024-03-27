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
    private final QuestionDao questionDao;

    @Override
    public void createQuiz(CreateQuizDto quizDto, Long applicantId) {
        Optional<User> userOptional = userDao.getUserById(applicantId);
        if (userOptional.isEmpty()) {
            log.error("Can't find user with id: " + applicantId);
        }
        Quiz quiz = makeQuiz(quizDto);
        return quizDao.createQuiz(quiz);
    }


}
