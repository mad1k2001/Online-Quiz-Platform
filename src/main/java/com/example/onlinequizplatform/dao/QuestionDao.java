package com.example.onlinequizplatform.dao;

import com.example.onlinequizplatform.models.Option;
import com.example.onlinequizplatform.models.Question;
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
public class QuestionDao {
    private final JdbcTemplate jdbcTemplate;

    public Long createQuestion(Question question) {
        String sql = "INSERT INTO questions (questionText, quizId) VALUES (?, ?)";
    public List<Question> getQuestionByQuizId(Long quizId) {
        String sql = """
                SELECT * FROM question WHERE quizId = ?
                """;
        return jdbcTemplate.query(sql, new Object[]{quizId}, new BeanPropertyRowMapper<>(Question.class));
    }

    public void createQuestion(Question question) {
        String sql = """
                INSERT INTO QUESTIONS (QUESTION_TEXT, QUIZ_ID) 
                VALUES (?,?)
                """;

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
}

        Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void updateQuestion(Question question){
        String sql = """
               UPDATE QUESTIONS
               SET QUESTION_TEXT = :questionText,
               QUIZ_ID = :quizId
               where ID = :id;
                """;
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("id", question.getId())
                .addValue("questionText", question.getQuestionText())
                .addValue("quizId", question.getQuizId()));
    }
}

