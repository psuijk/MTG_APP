package all.data;

import all.data.mappers.DeckMapper;
import all.models.Deck;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.List;

public class DeckJDBCRepository implements DeckRepository {
    private final JdbcTemplate jdbcTemplate;

    public DeckJDBCRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Override
    public List<Deck> findAll() {
        final String sql = "select deck_id, player_id, name, active, commander_id from deck limit 1000;";
        return jdbcTemplate.query(sql, new DeckMapper());
    }

    @Override
    public Deck findById(int deckId) {
        final String sql = "select deck_id, player_id, name, active, commander_id from deck where deck_id = ?;";
        Deck deck = jdbcTemplate.query(sql, new DeckMapper(), deckId).stream()
                .findFirst().orElse(null);

        return deck;
    }

    @Override
    public Deck add(Deck deck) {
        final String sql = "insert into deck (player_id, name, active, commander_id) values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, deck.getPlayerId());
            ps.setString(2, deck.getName());
            ps.setBoolean(3, deck.isActive());
            return ps;
        }, keyHolder);

        deck.setDeckId(keyHolder.getKey().intValue());
        return deck;
    }

    @Override
    public boolean update(Deck deck) {
        final String sql = "update deck set " +
                "player_id = ?, " +
                "name = ?, " +
                "active = ?, " +
                "commander_id = ?, " +
                "where deck_id = ?;";

        return jdbcTemplate.update(sql,
                deck.getPlayerId(),
                deck.getName(),
                deck.isActive(),
                deck.getCommanderId()) > 0;
    }

    @Override
    public boolean deleteById(int deckId) {
        //deck_id must not appear in any game_decks
        return jdbcTemplate.update("delete from deck where deck_id = ?;") > 0;
    }
}
