package com.example.onlinequizplatform.dao;

import com.example.onlinequizplatform.models.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorityDao {
    private final JdbcTemplate jdbcTemplate;

    public Optional<Authority> getRoles(String role) {
        String sql = """
                select * from AUTHORITIES
                where ROLE = ?;
                """;
        return Optional.ofNullable(DataAccessUtils.singleResult(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Authority.class), role)));
    }

}
