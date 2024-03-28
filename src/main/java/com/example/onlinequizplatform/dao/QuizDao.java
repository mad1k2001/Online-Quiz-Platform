package com.example.onlinequizplatform.dao;

import com.example.onlinequizplatform.models.Option;
import com.example.onlinequizplatform.models.Question;
import com.example.onlinequizplatform.models.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class QuizDao {
    private final JdbcTemplate jdbcTemplate;

    public List<Quiz> getQuizzes() {
        String sql = """
                select * from quizzes
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Quiz.class));
    }

    public Long createQuiz(Quiz quiz) {
        String sql = """
                INSERT INTO quizzes(title, desсription, creatorId)
                values (?,?,?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, quiz.getTitle());
            ps.setString(2, quiz.getDescription());
            ps.setLong(3, quiz.getCreatorId());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Quiz getQuizById(Long quizId) {
        String sql = "SELECT * FROM quizzes WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Quiz.class), quizId);
    }

    public void updateQuiz(Quiz quiz) {
        String sql = """
                UPDATE quizzes
                SET title = :title,
                description = :description,
                creatorId = :creatorId
                WHERE id = :id
                """;
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("title", quiz.getTitle())
                .addValue("description", quiz.getDescription())
                .addValue("creatorId", quiz.getDescription()));
    }

    public void updateQuizRating(Long quizId, Double rating) {
        String sql = "UPDATE quizzes SET rating = ? WHERE id = ?";
        jdbcTemplate.update(sql, rating, quizId);
    }
}
