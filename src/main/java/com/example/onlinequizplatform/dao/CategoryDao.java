package com.example.onlinequizplatform.dao;

import com.example.onlinequizplatform.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryDao {
    private final JdbcTemplate jdbcTemplate;
    public List<Category> getCategory() {
        String sql = """
                select * from categories
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }
    public Long createCategory(Category category) {
        String sql = """
                INSERT INTO quizzes(name)
                values (?,?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, category.getName());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
    public void updateCategory(Category category) {
        String sql = """
                UPDATE CATEGORIES
                SET name = :name
                WHERE id = :id
                """;
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("name", category.getName()));
    }
    public Optional<Category> getCategoryById(Long id){
        String sql = """
                SELECT * FROM categories WHERE id = ?
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class), id)
                .stream()
                .findFirst();
    }}
