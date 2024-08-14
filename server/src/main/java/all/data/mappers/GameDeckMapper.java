package all.data.mappers;

import all.models.GameDeck;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameDeckMapper implements RowMapper<GameDeck> {
    @Override
    public GameDeck mapRow(ResultSet resultSet, int i) throws SQLException {
        GameDeck gameDeck = new GameDeck();
        gameDeck.setGameDeckId(resultSet.getInt("game_deck_id"));
        gameDeck.setDeckId(resultSet.getInt("deck_id"));
        gameDeck.setGameId(resultSet.getInt("game_id"));
        gameDeck.setPosition(resultSet.getInt("position"));
        return gameDeck;

    }
}
