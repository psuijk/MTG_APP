package all.data.mappers;

import all.models.Player;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerMapper implements RowMapper<Player> {
    @Override
    public Player mapRow(ResultSet resultSet, int i)  throws SQLException {
        Player player = new Player();
        player.setPlayerId(resultSet.getInt("player_id"));
        player.setFirstName(resultSet.getString("first_name"));
        player.setLastName(resultSet.getString("last_name"));
        player.setUsername(resultSet.getString("username"));
        return player;
    }
}
