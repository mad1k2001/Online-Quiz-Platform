package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.QuestionDao;
import com.example.onlinequizplatform.models.Question;
import com.example.onlinequizplatform.models.Option;
import com.example.onlinequizplatform.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;

    @Override
    public Long createQuestion(Question question) {
        return questionDao.createQuestion(question);
    }

    @Override
    public Long createOption(Option option) {
        return questionDao.createOption(option);
    }
}