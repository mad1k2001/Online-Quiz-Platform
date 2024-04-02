package com.example.onlinequizplatform.controller;

import com.example.onlinequizplatform.dto.QuestionSolveDto;
import com.example.onlinequizplatform.dto.QuizDto;
import com.example.onlinequizplatform.dto.QuizResultAnsverDto;
import com.example.onlinequizplatform.dto.QuizResultDto;
import com.example.onlinequizplatform.dto.TopPlayersDto;
import com.example.onlinequizplatform.service.QuizResultService;
import com.example.onlinequizplatform.service.QuizService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
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

    @PostMapping("add")
    public ResponseEntity<QuizDto> createQuiz(Authentication authentication, @RequestBody @Valid QuizDto quizDto) {
        quizService.createQuiz(quizDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("/edit/quizzes/{quizzesId}")
    public HttpStatus updateQuiz(Authentication authentication, @PathVariable Long quizzesId, @RequestBody QuizDto quizDto){
        quizService.updateQuiz(quizDto, authentication, quizzesId);
        return HttpStatus.OK;
    }

    @PostMapping("/{quizId}/solve")
    public ResponseEntity<QuizResultAnsverDto> solve(@PathVariable Long quizId,
                                                     @Valid @RequestBody  List<QuestionSolveDto> questionSolveDtos,
                                                     Authentication auth) {
        return ResponseEntity.ok(quizService.solve(quizId, questionSolveDtos, auth));
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
    public ResponseEntity<Void> rateQuiz(@PathVariable Long quizId,
                                         @Min(value = 1, message = "minimum value 1")
                                         @Max(value = 5, message = "maximum value 5")
                                         Double rating,
                Authentication auth) {
        quizResultService.quizRating(quizId, rating, auth);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{quizId}/results")
    public ResponseEntity<QuizResultDto> getQuizResults(@PathVariable Long quizId, Authentication auth) {

        QuizResultDto quizResult = quizResultService.getQuizResultById(quizId, auth);
        if (quizResult == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(quizResult);
    }

    @GetMapping("/{quizId}/leaderboard")
    public ResponseEntity<List<QuizResultDto>> getQuizLeaderboard(@PathVariable Long quizId) {
        List<QuizResultDto> leaderboard = quizResultService.getQuizLeaderboard(quizId);
        if (leaderboard == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("topFivePlayers")
    public List<TopPlayersDto> topFivePlayers() {
        List<TopPlayersDto> topFivePlayers = quizResultService.topFivePlayers();
        return topFivePlayers;
    }

    @GetMapping("topTenPlayers")
    public List<TopPlayersDto> topTenPlayers() {
        List<TopPlayersDto> topTenPlayers = quizResultService.topTenPlayers();
        return  topTenPlayers;
    }
}


