package com.example.onlinequizplatform.dao;

import com.example.onlinequizplatform.models.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OptionDao {
    private final JdbcTemplate jdbcTemplate;

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

    public void updateOption(Option option) {
        String sql = "UPDATE options SET optionText = ?, isCorrect = ? WHERE id = ?";
        jdbcTemplate.update(sql, option.getOptionText(), option.getIsCorrect(), option.getId());
    }
}