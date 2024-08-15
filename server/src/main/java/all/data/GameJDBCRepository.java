package all.data;

import all.data.mappers.GameMapper;
import all.models.Game;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class GameJDBCRepository implements GameRepository {
    private final JdbcTemplate jdbcTemplate;

    public GameJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Game> findAll() {
        final String sql = "select game_id, date_played, winner_deck_id, player_count from game limit 1000";

        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    public Game findById(int gameId) {
        final String sql = "select game_id, date_played, winner_deck_id, player_count " +
                "from game " +
                "where game_id = ?;";
        return jdbcTemplate.query(sql, new GameMapper(), gameId)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public Game add(Game game) {
        final String sql = "insert into game (date_played, winner_deck_id, player_count) " +
                "values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(game.getDatePlayed()));
            ps.setInt(2, game.getWinnerDeckId());
            ps.setInt(3, game.getPlayerCount());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        game.setGameId(keyHolder.getKey().intValue());
        return game;
    }

    @Override
    public boolean update(Game game) {
        final String sql = "update game set " +
                "date_played = ?, " +
                "winner_deck_id = ?, " +
                "player_count = ? " +
                "where game_id = ?;";

        return jdbcTemplate.update(sql,
                game.getDatePlayed(),
                game.getWinnerDeckId(),
                game.getPlayerCount(),
                game.getGameId()) > 0;
    }

    @Override
    public boolean delete(int gameId) {
        return jdbcTemplate.update("delete from game where game_id = ?;", gameId) > 0;
    }
}
