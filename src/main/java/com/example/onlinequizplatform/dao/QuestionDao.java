package com.example.onlinequizplatform.dao;

import com.example.onlinequizplatform.models.Option;
import com.example.onlinequizplatform.models.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QuestionDao {
    private final JdbcTemplate jdbcTemplate;

    public Long createQuestion(Question question) {
        String sql = "INSERT INTO questions (questionText, quizId) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, question.getQuestionText());
            ps.setLong(2, question.getQuizId());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Long createOption(Option option) {
        String sql = "INSERT INTO options (optionText, isCorrect, questionId) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, option.getOptionText());
            ps.setBoolean(2, option.getIsCorrect());
            ps.setLong(3, option.getQuestionId());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
    public void updateQuestion(Question question) {
        String sql = "UPDATE questions SET questionText = ? WHERE id = ?";
        jdbcTemplate.update(sql, question.getQuestionText(), question.getId());
    }
    public List<Question> getQuestionsByQuizId(Long quizId) {
        String sql = "SELECT * FROM questions WHERE quiz_Id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Question.class), quizId);
    }

    public Optional<Question> getQuestionsByQuizIdAndQuestion(Long quizId, String  question) {
        String sql = "SELECT * FROM questions WHERE quiz_Id = ? and QUESTION_TEXT = ?";
        return Optional.ofNullable(DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Question.class),
                        quizId, question)));
    }
}