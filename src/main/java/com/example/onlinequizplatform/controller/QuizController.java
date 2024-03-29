package com.example.onlinequizplatform.controller;

import com.example.onlinequizplatform.dto.QuestionSolveDto;
import com.example.onlinequizplatform.dto.QuizDto;
import com.example.onlinequizplatform.dto.QuizResultDto;
import com.example.onlinequizplatform.service.QuizResultService;
import com.example.onlinequizplatform.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/quizzes")
public class QuizController {
    private final QuizService quizService;
    private final QuizResultService quizResultService;

    @GetMapping("")
    public ResponseEntity<List<QuizDto>> getQuizzes() {
        return ResponseEntity.ok(quizService.getQuizzes());
    }


    @PostMapping("add/{email}")
    public ResponseEntity<QuizDto> createQuiz(@PathVariable String email, @RequestBody QuizDto quizDto) {
        quizService.createQuiz(quizDto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("/edit/{email}/quizzes/{quizzesId}")
    public HttpStatus updateQuiz(@PathVariable String email, @PathVariable Long quizzesId, @RequestBody QuizDto quizDto){
        quizService.updateQuiz(quizDto,email,quizzesId);
        return HttpStatus.OK;
    }

    @PostMapping("/{quizId}/solve")
    public ResponseEntity<Void> solve(@PathVariable Long quizId, @RequestBody List<QuestionSolveDto> questionSolveDtos, Authentication auth
    ){
        String test= "test";
        quizService.solve(quizId, questionSolveDtos, auth);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Long quizId) {
        QuizDto quizDto = quizService.getQuizById(quizId);
        if (quizDto != null) {
            return ResponseEntity.ok(quizDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{quizId}/rate")
    public ResponseEntity<Void> rateQuiz(@PathVariable Long quizId, @RequestParam int correctAnswersCount, @RequestParam int totalQuestionsCount) {
        quizService.rateQuiz(quizId, correctAnswersCount, totalQuestionsCount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{quizId}/results")
    public ResponseEntity<QuizResultDto> resultQuiz(@PathVariable Long quizId) {
        QuizResultDto quizResultDto = quizResultService.getQuizResults(quizId);
        if (quizResultDto != null) {
            return ResponseEntity.ok(quizResultDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


