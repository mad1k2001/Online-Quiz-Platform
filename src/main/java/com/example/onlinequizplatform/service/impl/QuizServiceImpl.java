package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.QuestionDao;
import com.example.onlinequizplatform.dao.QuizDao;
import com.example.onlinequizplatform.dao.UserDao;
import com.example.onlinequizplatform.dto.OptionDto;
import com.example.onlinequizplatform.dto.QuestionDto;
import com.example.onlinequizplatform.dto.QuizDto;
import com.example.onlinequizplatform.models.Question;
import com.example.onlinequizplatform.models.Quiz;
import com.example.onlinequizplatform.models.User;
import com.example.onlinequizplatform.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        for (Quiz quiz : quizzes) {
            QuizDto quizDto = makeQuizDto(quiz);
            List<QuestionDto> questionDtoList = questionDao.getQuestionByQuizId(quiz.getId()).stream()
                    .map(this::makeQuestionDto)
                    .collect(Collectors.toList());
            quizDto.setQuestions(questionDtoList);
            dto.add(quizDto);
        }
        return dto;
    }

    @Override
    public Long createQuiz(QuizDto quizDto, String email) {
        Quiz quiz = makeQuiz(quizDto);
        if (quizDto.getQuestions() != null) {
            for (QuestionDto questionDto : quizDto.getQuestions()) {
                questionDao.createQuestion(makeQuestion(questionDto));
            }
        }
        return quizDao.createQuiz(quiz);
    }

    @Override
    public void updateQuiz(QuizDto quizDto, String email, Long quizzesId){
        Quiz quiz = makeQuiz(quizDto);
        quizDao.updateQuiz(quiz);
        updateQuestion(quizDto.getQuestions(), quizzesId);
    }

    private void updateQuestion(List<QuestionDto> questionDtoList, Long quizzesId) {
        if (questionDtoList == null) return;

        List<Question> existingQuestionList = questionDao.getQuestionByQuizId(quizzesId);

        for (QuestionDto questionDto : questionDtoList) {
            existingQuestionList.stream()
                    .filter(existingQuestion-> existingQuestion.getId().equals(questionDto.getId()))
                    .forEach(existingQuestion -> {
                        existingQuestion.setQuestionText(questionDto.getQuestionText());
                        questionDao.updateQuestion(existingQuestion);
                    });
        }
    }

    @Override
    public Long createQuestionForQuiz(Long quizId, QuestionDto questionDto) {

        Question question = makeQuestion(questionDto, quizId);
        return questionDao.createQuestion(question);
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


    private Question makeQuestion(QuestionDto questionDto, Long quizId) {
        return Question.builder()
                .questionText(questionDto.getQuestionText())
                .quizId(quizId)
                .build();
    }
}

    private Question makeQuestion(QuestionDto questionDto) {
        return Question.builder()
                .id(questionDto.getId())
                .questionText(questionDto.getQuestionText())
                .quizId(questionDto.getQuizId())
                .build();
    }

    private QuestionDto makeQuestionDto(Question question){
        return QuestionDto.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .quizId(question.getQuizId())
                .build();
    }

}
