package all.data;

import all.data.mappers.GameDeckMapper;
import all.models.GameDeck;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class GameDeckJDBCRepository implements GameDeckRepository {
    private final JdbcTemplate jdbcTemplate;

    public GameDeckJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GameDeck> findAll() {
        final String sql = "select game_deck_id, game_id, deck_id, position from game_deck limit 1000;";

        return jdbcTemplate.query(sql, new GameDeckMapper());
    }

    @Override
    public List<GameDeck> findByDeckId(int deckId) {
        final String sql = "select game_deck_id, game_id, deck_id, position from game_deck where deck_id = ?;";

        return jdbcTemplate.query(sql, new GameDeckMapper(), deckId);
    }

    @Override
    public List<GameDeck> findByGameId(int gameId) {
        final String sql = "select game_deck_id, game_id, deck_id, position from game_deck where game_id = ?;";

        return jdbcTemplate.query(sql, new GameDeckMapper(), gameId);
    }

    @Override
    public GameDeck findById(int gameDeckId) {
        final String sql = "select game_deck_id, game_id, deck_id, position from game_deck where game_deck_id = ?;";

        return jdbcTemplate.query(sql, new GameDeckMapper(), gameDeckId)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public GameDeck add(GameDeck gameDeck) {
        final String sql = "insert into game_deck (game_id, deck_id, position) " +
                "values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, gameDeck.getGameId());
            ps.setInt(2, gameDeck.getDeckId());
            ps.setInt(3, gameDeck.getPosition());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        gameDeck.setGameDeckId(keyHolder.getKey().intValue());

        return gameDeck;
    }

    @Override
    public boolean update(GameDeck gameDeck) {
        final String sql = "update game_deck set " +
                "game_id = ?, " +
                "deck_id = ?, " +
                "position = ? " +
                "where game_deck_id = ?;";

        return jdbcTemplate.update(sql,
                gameDeck.getGameId(),
                gameDeck.getDeckId(),
                gameDeck.getPosition(),
                gameDeck.getGameDeckId()) > 0;
    }

    @Override
    public boolean deleteById(int gameDeckId) {
        return jdbcTemplate.update("delete from game_deck where game_deck_id = ?;", gameDeckId) > 0;
    }
}
