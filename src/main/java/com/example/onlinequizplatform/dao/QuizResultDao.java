package com.example.onlinequizplatform.dao;

import com.example.onlinequizplatform.dto.OptionDto;
import com.example.onlinequizplatform.dto.QuestionDto;
import com.example.onlinequizplatform.models.Quiz;
import com.example.onlinequizplatform.models.QuizResult;
import com.example.onlinequizplatform.models.TopPlayers;
import com.example.onlinequizplatform.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public Optional<QuizResult> getQuizResultById(Long quizId, Long userId) {
        String sql = "SELECT * FROM QUIZ_RESULTS WHERE QUIZ_ID = ? and USER_ID = ?";
        var result = Optional.ofNullable(DataAccessUtils.singleResult(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResult.class), quizId, userId)));
        return result;
    }

    public List<QuizResult> getQuizResultsByQuizId(Long quizId) {
        String sql = "SELECT * FROM QUIZ_RESULTS WHERE QUIZ_ID = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResult.class), quizId);
    }

    public List<QuizResult> getQuizResultByUserId(Long userId) {
        String sql = "SELECT * FROM QUIZ_RESULTS WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResult.class), userId);
    }

    public void updateQuizRating(Long quizId, Double rating, Long userId) {
        String sql = "UPDATE QUIZ_RESULTS SET QUIZ_RATING = ? WHERE QUIZ_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sql, rating, quizId, userId);
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

    public void clearBestPlayersTable(){
        String clearTable = "delete from BEST_PLAYERS;";
        jdbcTemplate.update(clearTable);
    }

    public void insertTenBestPlayers() {
        String insertBestPl = """
                INSERT INTO BEST_PLAYERS (POSITION, USER_NAME, SCORE, USER_ID)
                    select ROW_NUMBER() Over (Order by u.score desc) As position, u.* from  (
                        select top(10)   u.NAME as USER_NAME , sum(score) as SCORE,  u.id as USER_ID from QUIZ_RESULTS q                                                                                                                                      LEFT JOIN USERS u ON q.USER_ID= u.ID
                group by USER_ID
                ) u
                """;
        jdbcTemplate.update(insertBestPl);
    }

    public List<TopPlayers> topFivePlayers() {
        String sql =  """
                select top(5) * from BEST_PLAYERS
                                     order by SCORE desc
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TopPlayers.class));
    }

    public List<TopPlayers> topTenPlayers() {
        String sql =  """
                select top(10) * from BEST_PLAYERS
                                     order by SCORE desc
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TopPlayers.class));
    }
}
