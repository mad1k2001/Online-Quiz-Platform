package com.example.onlinequizplatform.dao;

import com.example.onlinequizplatform.models.QuizResult;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuizResultDao {
    private final JdbcTemplate jdbcTemplate;


    public List<QuizResult> getResultsByUserEmail(String email){
        String sql = """
                SELECT * FROM quiz_results q
                left join user u on u.id = q.user_id 
                WHERE u.email = ? ;
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResult.class), email);
    }

    public boolean isAnswered(String email, Long quizId){
        String sql = """
                SELECT
                     CASE WHEN (count(*) >0) THEN false ELSE true END as result
                 FROM quiz_results q
                left join user u on u.id = q.user_id 
                WHERE u.email = ? 
                and q.quiz_id = ?;
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, email, quizId);
    }
}
