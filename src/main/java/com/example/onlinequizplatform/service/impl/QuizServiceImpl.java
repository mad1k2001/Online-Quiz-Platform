package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.OptionDao;
import com.example.onlinequizplatform.dao.QuestionDao;
import com.example.onlinequizplatform.dao.QuizDao;
import com.example.onlinequizplatform.dto.*;
import com.example.onlinequizplatform.exeptions.CustomException;
import com.example.onlinequizplatform.models.Option;
import com.example.onlinequizplatform.models.Question;
import com.example.onlinequizplatform.models.Quiz;

import com.example.onlinequizplatform.service.QuizResultService;
import com.example.onlinequizplatform.service.QuizService;
import com.example.onlinequizplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final OptionDao optionDao;
    private final UserService userService;
    private final QuizDao quizDao;
    private final QuestionDao questionDao;
    private final QuizResultService quizResultService;

    @Override
    public List<QuizDto> getQuizzes() {
        List<Quiz> quizzes = quizDao.getQuizzes();
        List<QuizDto> quizDtos = new ArrayList<>();
        for (Quiz quiz : quizzes) {
            QuizDto quizDto = makeQuizDto(quiz);
            List<Question> questions = questionDao.getQuestionsByQuizId(quiz.getId());
            List<QuestionDto> questionDtos = new ArrayList<>();
            for (Question question : questions) {
                List<Option> options = optionDao.getOptionsByQuestionId(question.getId());
                List<OptionDto> optionDtos = new ArrayList<>();
                for (Option option : options) {
                    OptionDto optionDto = makeOptionDto(option);
                    optionDtos.add(optionDto);
                }
                QuestionDto questionDto = makeQuestionDto(question, optionDtos);
                questionDtos.add(questionDto);
            }
            quizDto.setQuestions(questionDtos);
            quizDtos.add(quizDto);
        }
        return quizDtos;
    }
    @Override
    public Long createQuiz(QuizDto quizDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserDto currentUser=userService.getUserByEmail(user.getUsername());
        if (user == null) {
            throw new CustomException("User not found");
        }

        Quiz quiz = makeQuiz(quizDto);
        quiz.setCreatorId(currentUser.getId());

        Long quizId = quizDao.createQuiz(quiz);

        if (quizDto.getQuestions() != null) {
            for (QuestionDto questionDto : quizDto.getQuestions()) {
                Long questionId = createQuestionForQuiz(quizId, questionDto);
            }
        }

        return quizId;
    }
    @Override
    public void updateQuiz(QuizDto quizDto, Authentication authentication, Long quizId) {
        User user = (User) authentication.getPrincipal();
        UserDto currentUser=userService.getUserByEmail(user.getUsername());
        if (user == null) {
            throw new CustomException("User not found");
        }

        Quiz quiz = makeQuiz(quizDto);
        quiz.setId(quizId);
        quiz.setCreatorId(currentUser.getId());

        quizDao.updateQuiz(quiz);

        if (quizDto.getQuestions() != null) {
            for (QuestionDto questionDto : quizDto.getQuestions()) {
                updateQuestionForQuiz(quizId, questionDto);
            }
        }
    }
    public Long createQuestionForQuiz(Long quizId, QuestionDto questionDto) {
        Question question = makeQuestion(questionDto, quizId);
        return questionDao.createQuestion(question);
    }
    public void updateQuestionForQuiz(Long quizId, QuestionDto questionDto){
        Question question = makeQuestion(questionDto,quizId);
        questionDao.updateQuestion(question);
    }
    private Quiz makeQuiz(QuizDto quizDto){
        return Quiz.builder()
                .title(quizDto.getTitle())
                .description(quizDto.getDescription())
                .creatorId(quizDto.getCreatorId())
                .categoryId(quizDto.getCategoryId())
                .build();
    }
    private QuizDto makeQuizDto(Quiz quiz){
        return QuizDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .creatorId(quiz.getCreatorId())
                .categoryId(quiz.getCategoryId())
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
                    OptionDto optionDto = makeOptionDto(option);
                    optionDto.setIsCorrect(null);
                    optionDtos.add(optionDto);
                }
                QuestionDto questionDto = makeQuestionDto(question, optionDtos);
                questionDtos.add(questionDto);
            }
            quizDto.setQuestions(questionDtos);
            return quizDto;
        }
        return null;
    }
    @Override
    public QuizResultAnsverDto solve(Long quizId, List<QuestionSolveDto> questionSolveDtos, Authentication auth) {
        User user = (User) auth.getPrincipal();
        String authorEmail=user.getUsername();

        UserDto currentUser=userService.getUserByEmail(authorEmail);

        List<Question> questions = questionDao.getQuestionsByQuizId(quizId);
        if(questions.stream().count()!=questionSolveDtos.stream().count()){
            String message="You haven't answered all the questions";
            log.error(message);
            throw new CustomException(message);
        }

        quizResultService.getResultsByUserEmail(authorEmail);
        if(quizResultService.isAnsweredQuiz(authorEmail,quizId)){
            String message="User cannot pass quiz because he has already passed it";
            log.error(message);
            throw new CustomException(message);
        }
       AtomicInteger isCorrectAnsver= new AtomicInteger();
       AtomicInteger unCorrectAnsver= new AtomicInteger();

        questionSolveDtos.forEach(f->{
            String question=f.getQuestionText();
            Optional<Question> questionRes=questionDao.getQuestionsByQuizIdAndQuestion(quizId, f.getQuestionText());
            if(questionRes.isEmpty()){
                String message="This error question: "+f.getQuestionText();
                log.error(message);
                throw new CustomException(message);
            }

                Optional<Option> resOp= optionDao.getOptionsByQuestionText(questionRes.get().getId(), f.getAnswers());
                if(resOp.isEmpty()){
                    String message="This error answer: "+f.getAnswers();
                    log.error(message);
                    throw new CustomException(message);
                }
                if(resOp.get().getIsCorrect()){
                    isCorrectAnsver.getAndIncrement();
                }
                else {
                    unCorrectAnsver.getAndIncrement();
                }

        });

        BigDecimal score = BigDecimal.valueOf(isCorrectAnsver.get())
                .divide(BigDecimal.valueOf(questions.stream().count()))
                .multiply(BigDecimal.valueOf(5));

        quizResultService.createQuizResult(score, quizId, currentUser.getId(), isCorrectAnsver.get(), (int)questions.stream().count());
        return QuizResultAnsverDto.builder()
                .correctAnswers(isCorrectAnsver.get())
                .score(score)
                .totalQuestions((int)questions.stream().count())
                .unCorrectAnswers(unCorrectAnsver.get())
                .build();
    }
    @Override
    public List<QuestionDto> getQuestionsByQuizIdWithPagination(Long quizId, int page, int size) {
        int offset = page * size;
        List<Question> questions = questionDao.getQuestionsByQuizIdWithPagination(quizId, offset, size);
        List<QuestionDto> questionDtos = new ArrayList<>();
        for (Question question : questions) {
            List<Option> options = optionDao.getOptionsByQuestionId(question.getId());
            List<OptionDto> optionDtos = new ArrayList<>();
            for (Option option : options) {
                OptionDto optionDto = makeOptionDto(option);
                optionDtos.add(optionDto);
            }
            QuestionDto questionDto = makeQuestionDto(question, optionDtos);
            questionDtos.add(questionDto);
        }
        return questionDtos;
    }}
