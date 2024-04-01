package com.example.onlinequizplatform.dao;

import com.example.onlinequizplatform.dto.OptionDto;
import com.example.onlinequizplatform.dto.QuestionDto;
import com.example.onlinequizplatform.models.Quiz;
import com.example.onlinequizplatform.models.QuizResult;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class QuizResultDao {
    private final JdbcTemplate jdbcTemplate;


    public List<QuizResult> getResultsByUserEmail(String email){
        String sql = """
                SELECT * FROM quiz_results q
                left join users u on u.id = q.user_id 
                WHERE u.email = ? ;
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResult.class), email);
    }

    public boolean isAnswered(String email, Long quizId){
        String sql = """
                SELECT
                     CASE WHEN (count(*) >0) THEN true ELSE false END as result
                 FROM quiz_results q
                left join users u on u.id = q.user_id 
                WHERE u.email = ? 
                and q.quiz_id = ?;
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, email, quizId);
    }

    public QuizResult getQuizResultById(Long resultId) {
        String sql = "SELECT * FROM quiz_results WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(QuizResult.class), resultId);
    }


    public void updateQuizRating(Long quizId, Double rating) {
        String sql = "UPDATE QUIZ_RESULTS SET QUIZ_RATING = ? WHERE id = ?";
        jdbcTemplate.update(sql, rating, quizId);
    }



    public Long createQuizResult(QuizResult quiz) {
        String sql = """
                INSERT INTO QUIZ_RESULTS(SCORE, QUIZ_ID, USER_ID, CORRECT_ANSWERS, TOTAL_QUESTIONS)
                values (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setBigDecimal(1, quiz.getScore());
            ps.setLong(2, quiz.getQuizId());
            ps.setLong(3, quiz.getUserId());
            ps.setInt(4, quiz.getCorrectAnswers());
            ps.setInt(5, quiz.getTotalQuestions());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
