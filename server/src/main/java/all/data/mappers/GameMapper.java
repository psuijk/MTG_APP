package all.data.mappers;

import all.models.Game;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameMapper implements RowMapper<Game> {
    @Override
    public Game mapRow(ResultSet resultSet, int i) throws SQLException {
        Game game = new Game();
        game.setGameId(resultSet.getInt("game_id"));
        game.setDatePlayed(resultSet.getDate("date_played").toLocalDate());
        game.setWinnerDeckId(resultSet.getInt("player_count"));
        return game;
    }

}
