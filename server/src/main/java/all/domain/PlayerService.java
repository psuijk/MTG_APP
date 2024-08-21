package all.domain;

import all.data.AppUserRepository;
import all.data.PlayerJDBCRepository;
import all.data.PlayerRepository;
import all.models.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    PlayerRepository playerRepo;
    AppUserRepository userRepo;

    public PlayerService(PlayerRepository playerRepo, AppUserRepository userRepo) {
        this.playerRepo = playerRepo;
        this.userRepo = userRepo;
    }

    public List<Player> findAll() { return this.playerRepo.findAll(); }

    public Player findById(int playerId) { return this.playerRepo.findById(playerId); }
    public Player findByUsername(String username) { return this.playerRepo.findByUsername(username); }

    public Result<Player> add(Player player) {
        Result<Player> result = validate(player);

        if (!result.isSuccess()) {
            return result;
        }

        if (player.getPlayerId() != 0) {
            result.addMessage("playerId cannot be set for 'add' operation", ResultType.INVALID);
            return result;
        }

        player = playerRepo.add(player);
        result.setPayload(player);

        return result;
    }

    public Result<Player> update(Player player) {
        Result<Player> result = validate(player);

        if (!result.isSuccess()) {
            return result;
        }

        Player check = playerRepo.findById(player.getPlayerId());

        if (check == null) {
            result.addMessage(String.format("playerId: %s not found", player.getPlayerId()), ResultType.NOT_FOUND);
            return result;
        }

        if (!playerRepo.update(player)) {
            result.addMessage(String.format("Error updating playerId: %s", player.getPlayerId()), ResultType.INVALID);
        }

        return result;
    }

    public Result<Player> delete(Player player) {
        Result<Player> result = new Result<>();

        if (playerRepo.findById(player.getPlayerId()) == null) {
            result.addMessage(String.format("player with id %s not found", player.getPlayerId()), ResultType.NOT_FOUND);
            return result;
        }

        if (player.getPlayerDecks() != null  && !player.getPlayerDecks().isEmpty()) {
            result.addMessage("cannot delete player with existing decks", ResultType.INVALID);
            return result;
        }

        if (userRepo.findByUsername(player.getUsername()) != null) {
            result.addMessage("cannot delete player with existing account", ResultType.INVALID);
            return result;
        }


        if (!playerRepo.delete(player)) {
            result.addMessage(String.format("Error deleting player with id %s", player.getPlayerId()), ResultType.FAILURE);
        }

        return result;
    }

    private Result<Player> validate(Player player) {
        Result<Player> result = new Result<>();

        if (player.getFirstName().isEmpty()) {
            result.addMessage("first name is required", ResultType.INVALID);
            return result;
        }

        if (player.getLastName().isEmpty()) {
            result.addMessage("last name is required", ResultType.INVALID);
            return result;
        }

        /*if (player.getUsername().isEmpty()) {
            result.addMessage("username is required", ResultType.INVALID);
            return result;
        }*/

        return result;
    }
}
