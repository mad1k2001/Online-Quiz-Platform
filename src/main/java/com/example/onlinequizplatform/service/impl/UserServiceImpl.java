package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.QuizResultDao;
import com.example.onlinequizplatform.dao.UserDao;
import com.example.onlinequizplatform.dto.UserCreateDto;
import com.example.onlinequizplatform.dto.UserDto;
import com.example.onlinequizplatform.dto.UserStatisticsDto;
import com.example.onlinequizplatform.exeptions.CustomException;
import com.example.onlinequizplatform.models.QuizResult;
import com.example.onlinequizplatform.models.User;
import com.example.onlinequizplatform.service.AuthorityService;
import com.example.onlinequizplatform.service.QuizResultService;
import com.example.onlinequizplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final QuizResultDao quizResultDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;


    @Override
    public void registerUser(UserCreateDto userCreateDto) {
        Optional<User> userCheck = userDao.getUsersByEmail(userCreateDto.getEmail());
        if(!userCheck.isEmpty()){
            String error="There is already a user with this email";
            log.error(error);
            throw new CustomException(error);
        }
        User user = new User();
        user.setName(userCreateDto.getName());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setRoleId(authorityService.getRoles("ADMIN").getId());

        userDao.save(user);
    }

    @Override
    public UserDto getUserByEmail(String email){
        Optional<User> user = userDao.getUsersByEmail(email);
        if(user.isEmpty()){
            String error="User is not found";
            log.error(error);
            throw new CustomException(error);
        }
        return UserDto.builder()
                .id(user.get().getId())
                .name(user.get().getName())
                .email(user.get().getEmail())
                .build();
    }

    @Override
    public UserStatisticsDto getUserStatistics(Long userId) {
        List<QuizResult> quizResults = quizResultDao.getQuizResultByUserId(userId);

        int totalQuizzes = quizResults.size();
        BigDecimal totalScore = BigDecimal.ZERO;
        BigDecimal maxScore = BigDecimal.ZERO;
        BigDecimal minScore = BigDecimal.ZERO;
        for (QuizResult quizResult : quizResults) {
            BigDecimal score = quizResult.getScore();
            totalScore = totalScore.add(score);
            if (score.compareTo(maxScore) > 0) {
                maxScore = score;
            }
            if (score.compareTo(minScore) < 0 || minScore.equals(BigDecimal.ZERO)) {
                minScore = score;
            }
        }

        BigDecimal averageScore = totalQuizzes > 0 ?
                totalScore.divide(BigDecimal.valueOf(totalQuizzes), 2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        log.debug("User statistics calculated: userId={}, totalQuizzes={}, averageScore={}, maxScore={}, minScore={}",
                userId, totalQuizzes, averageScore, maxScore, minScore);

        return UserStatisticsDto.builder()
                .totalQuizzes(totalQuizzes)
                .averageScore(averageScore)
                .maxScore(maxScore)
                .minScore(minScore)
                .build();
    }
}
