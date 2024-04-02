package com.example.onlinequizplatform.dao;

import com.example.onlinequizplatform.models.Quiz;
import com.example.onlinequizplatform.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public void save(User user) {
        String sql = "INSERT INTO users (name, email, password, role_id) VALUES (?, ?, ?,?)";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword(), user.getRoleId());
    }

    public Optional<User> getUsersByEmail(String email) {
        String sql = """
                SELECT * FROM USERS 
                WHERE EMAIL = ?;
                """;
        var result = Optional.ofNullable(DataAccessUtils.singleResult(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email)));
        return result;
    }
}
