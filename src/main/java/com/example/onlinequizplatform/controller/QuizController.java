package com.example.onlinequizplatform.controller;

import com.example.onlinequizplatform.dto.CreateQuizDto;
import com.example.onlinequizplatform.dto.QuestionDto;
import com.example.onlinequizplatform.dto.QuizDto;
import com.example.onlinequizplatform.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/quizzes")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("")
    public ResponseEntity<List<QuizDto>> getQuizzes() {
        return ResponseEntity.ok(quizService.getQuizzes());
    }


    @PostMapping("add/{email}")
    public ResponseEntity<QuizDto> createQuiz(@PathVariable String email, @RequestBody CreateQuizDto createQuizDto) {
        quizService.createQuiz(createQuizDto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }




//    @GetMapping
//    public ResponseEntity<List<QuizDto>> getAllQuizzes() {
//        List<QuizDto> quizzes = quizService.getAllQuizzes();
//        return ResponseEntity.ok(quizzes);
//    }
}
