package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.OptionDao;
import com.example.onlinequizplatform.dao.QuestionDao;
import com.example.onlinequizplatform.dao.QuizDao;
import com.example.onlinequizplatform.dao.UserDao;
import com.example.onlinequizplatform.dto.OptionDto;
import com.example.onlinequizplatform.dto.QuestionDto;
import com.example.onlinequizplatform.dto.QuestionSolveDto;
import com.example.onlinequizplatform.dto.QuizDto;
import com.example.onlinequizplatform.dto.UserDto;
import com.example.onlinequizplatform.exeptions.CustomException;
import com.example.onlinequizplatform.models.Question;
import com.example.onlinequizplatform.models.Quiz;
import com.example.onlinequizplatform.models.Option;
import com.example.onlinequizplatform.service.QuizResultService;

import com.example.onlinequizplatform.service.QuizService;
import com.example.onlinequizplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final UserService userService;
    private final QuizDao quizDao;
    private final QuestionDao questionDao;
    private final OptionDao optionDao;
    private final QuizResultService quizResultService;

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

    private OptionDto makeOptionDto(Option option) {
        return OptionDto.builder()
                .id(option.getId())
                .optionText(option.getOptionText())
                .isCorrect(option.getIsCorrect())
                .questionId(option.getQuestionId())
                .build();
    }

    private QuestionDto makeQuestionDto(Question question, List<OptionDto> options) {
        return QuestionDto.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .quizId(question.getQuizId())
                .option(options)
                .build();
    }

    @Override
    public QuizDto getQuizById(Long quizId) {
        Quiz quiz = quizDao.getQuizById(quizId);
        if (quiz != null) {
            QuizDto quizDto = makeQuizDto(quiz);
            List<Question> questions = questionDao.getQuestionsByQuizId(quizId);
            List<QuestionDto> questionDtos = new ArrayList<>();
            for (Question question : questions) {
                List<Option> options = optionDao.getOptionsByQuestionId(question.getId());
                List<OptionDto> optionDtos = new ArrayList<>();
                for (Option option : options) {
                    if (!option.getIsCorrect()) {
                        optionDtos.add(makeOptionDto(option));
                    }
    }

    @Override
    public void solve(Long quizId, List<QuestionSolveDto> questionSolveDtos, Authentication auth) {
        User user = (User) auth.getPrincipal();
        String authorEmail=user.getUsername();
        QuizDto quizDto = getQuizById(quizId);
        UserDto userDto = userService.getUserByEmail(authorEmail);
        quizResultService.getResultsByUserEmail(authorEmail);
            if(quizResultService.isAnsweredQuiz(authorEmail,quizId)){
                throw new CustomException ("User cannot pass quiz because he has already passed it");
            }

                }
                QuestionDto questionDto = makeQuestionDto(question, optionDtos);
                questionDtos.add(questionDto);
            }
            quizDto.setQuestions(questionDtos);
            return quizDto;
        }
        return null;
    }
}