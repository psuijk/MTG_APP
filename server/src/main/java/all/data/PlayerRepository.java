package all.data;

import all.models.Player;

import java.util.List;

public interface PlayerRepository {
    List<Player> findAll();
    Player findById(int playerId);
    Player findByUsername(String username);
    Player add(Player player);
    boolean update(Player player);
    boolean delete(Player player);
}