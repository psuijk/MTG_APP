package all.data.mappers;

import all.models.Deck;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeckMapper implements RowMapper<Deck> {
    @Override
    public Deck mapRow(ResultSet resultSet, int i) throws SQLException {
        Deck deck = new Deck();
        deck.setDeckId(resultSet.getInt("deck_id"));
        deck.setPlayerId(resultSet.getInt("player_id"));
        deck.setName(resultSet.getString("name"));
        deck.setActive(resultSet.getBoolean("active"));
        deck.setCommanderId(resultSet.getString("commander_id"));
        return deck;
    }
}
