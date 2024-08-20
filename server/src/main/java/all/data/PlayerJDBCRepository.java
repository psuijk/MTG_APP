package all.data;

import all.data.mappers.DeckMapper;
import all.data.mappers.PlayerMapper;
import all.models.Player;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class PlayerJDBCRepository implements PlayerRepository {
    private final JdbcTemplate jdbcTemplate;

    public PlayerJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Player> findAll() {
        final String sql = "select player_id, first_name, last_name, username from player limit 1000;";

        List<Player> players = jdbcTemplate.query(sql, new PlayerMapper());

        for (Player player : players) {
            if (player != null) {
                addDecks(player);
            }
        }

        return players;
    }

    @Override
    @Transactional
    public Player findById(int playerId) {
        final String sql = "select player_id, first_name, last_name, username " +
                "from player " +
                "where player_id = ?;";

        Player player = jdbcTemplate.query(sql, new PlayerMapper(), playerId)
                .stream()
                .findFirst().orElse(null);

        if (player != null) {
            addDecks(player);
        }

        return player;
    }

    @Override
    @Transactional
    public Player findByUsername(String username) {
        final String sql = "select player_id, first_name, last_name, username " +
                "from player " +
                "where username = ?;";

        Player player = jdbcTemplate.query(sql, new PlayerMapper(), username)
                .stream()
                .findFirst().orElse(null);

        if (player != null) {
            addDecks(player);
        }

        return player;
    }

    @Override
    public Player add(Player player) {
        final String sql = "insert into player (first_name, last_name, username) " +
                "values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, player.getFirstName());
            ps.setString(2, player.getLastName());
            ps.setString(3, player.getUsername());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        player.setPlayerId(keyHolder.getKey().intValue());
        return player;
    }

    @Override
    public boolean update(Player player) {
        final String sql = "update player set " +
                "first_name = ?, " +
                "last_name = ?, " +
                "username = ? " +
                "where player_id = ?;";
        return jdbcTemplate.update(sql,
                player.getFirstName(),
                player.getLastName(),
                player.getUsername(),
                player.getPlayerId()) > 0;
    }

    @Override
    public boolean delete(Player player) {
        return jdbcTemplate.update("delete from player where player_id = ?", player.getPlayerId()) > 0;
    }

    private void addDecks(Player player) {
        final String sql = "select d.deck_id, d.player_id, d.name, d.active, d.commander_id " +
                "from player p " +
                "join " +
                "deck d on p.player_id = d.player_id " +
                "where p.player_id = ?;";

        var decks = jdbcTemplate.query(sql, new DeckMapper(), player.getPlayerId());
        player.setPlayerDecks(decks);

    }
}
