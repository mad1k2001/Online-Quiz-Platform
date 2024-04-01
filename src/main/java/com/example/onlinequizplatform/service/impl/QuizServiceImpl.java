package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.OptionDao;
import com.example.onlinequizplatform.dao.QuestionDao;
import com.example.onlinequizplatform.dao.QuizDao;
import com.example.onlinequizplatform.dto.*;
import com.example.onlinequizplatform.exeptions.CustomException;
import com.example.onlinequizplatform.models.Option;
import com.example.onlinequizplatform.models.Question;
import com.example.onlinequizplatform.models.Quiz;

import com.example.onlinequizplatform.models.QuizResult;
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

        questionSolveDtos.forEach(f->{
            String question=f.getQuestionText();
            Optional<Question> questionRes=questionDao.getQuestionsByQuizIdAndQuestion(quizId, f.getQuestionText());
            if(questionRes.isEmpty()){
                String message="This error question: "+f.getQuestionText();
                log.error(message);
                throw new CustomException(message);
            }

            f.getAnswers().forEach(an->{
                Optional<Option> resOp= optionDao.getOptionsByQuestionText(questionRes.get().getId(),an.getOptionText());
                if(resOp.isEmpty()){
                    String message="This error answer: "+an.getOptionText();
                    log.error(message);
                    throw new CustomException(message);
                }
                if(resOp.get().getIsCorrect()){
                    isCorrectAnsver.getAndIncrement();
                }
            });
        });

        BigDecimal score = BigDecimal.valueOf(isCorrectAnsver.get())
                .divide(BigDecimal.valueOf(questions.stream().count()))
                .multiply(BigDecimal.valueOf(5));

        quizResultService.createQuizResult(score, quizId, currentUser.getId(), isCorrectAnsver.get(), (int)questions.stream().count());
        return QuizResultAnsverDto.builder()
                .correctAnswers(isCorrectAnsver.get())
                .score(score)
                .totalQuestions((int)questions.stream().count())
                .build();

    }

    @Override
    public void rateQuiz(Long quizId, int correctAnswersCount, int totalQuestionsCount) {

        Double rating = (double) correctAnswersCount / totalQuestionsCount * 5.0;


        quizResultService.updateQuizRating(quizId, rating);
    }


}
