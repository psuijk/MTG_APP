package all.domain;

import all.data.GameDeckRepository;
import all.data.GameRepository;
import all.data.PlayerRepository;
import all.models.Deck;
import all.models.Game;
import all.models.GameDeck;
import all.models.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    GameDeckRepository gameDeckRepo;
    GameRepository gameRepo;
    PlayerRepository playerRepo;

    public GameService (GameDeckRepository gameDeckRepo, GameRepository gameRepo, PlayerRepository playerRepo) {
        this.gameDeckRepo = gameDeckRepo;
        this.gameRepo = gameRepo;
        this.playerRepo = playerRepo;
    }

    public List<Game> findAll() { return gameRepo.findAll(); }

    public Game findById(int gameId) { return gameRepo.findById(gameId); }

    public List<Game> findByDeckId(int deckId) {
        List<GameDeck> gameDecks = gameDeckRepo.findByDeckId(deckId);
        List<Game> games = new ArrayList<>();
        for (GameDeck gd : gameDecks) {
            games.add(gameRepo.findById(gd.getGameId()));
        }

        return games;
    }

    public List<Game> findByPlayer(int playerId) {
        List<Game> all = new ArrayList<>();
        Player player = playerRepo.findById(playerId);
        if (!player.getPlayerDecks().isEmpty()) {
            for (Deck d : player.getPlayerDecks()) {
                for (GameDeck gameDeck : gameDeckRepo.findByDeckId(d.getDeckId())) {
                    all.add(gameRepo.findById(gameDeck.getGameId()));
                }
            }
        }

        return all;
    }

    public Result<Game> add(Game game) {
        Result<Game> result = validate(game);

        if (!result.isSuccess()) {
            return result;
        }

        if (game.getGameId() != 0) {
            result.addMessage("game id cannot be pre-set", ResultType.INVALID);
            return result;
        }

        game = gameRepo.add(game);

        if (game == null) {
            result.addMessage("something went wrong adding game", ResultType.FAILURE);
            return result;
        }
        result.setPayload(game);

        for (int i = 0; i < game.getPlayerCount(); i++) {
            GameDeck gameDeck = gameDeckRepo.add(new GameDeck(0,game.getGameId(), game.getDecks()[i], i + 1));
            if (gameDeck == null) {
                result.addMessage(String.format("Error adding game %s", game.getGameId()), ResultType.FAILURE);
                return result;
            }
        }

        return result;
    }

    public Result<Game> update(Game game) {
        Result<Game> result = validate(game);

        if (!result.isSuccess()) {
            return result;
        }

        Game check = gameRepo.findById(game.getGameId());

        if (check == null) {
            result.addMessage(String.format("game id: %s not found", game.getGameId()), ResultType.NOT_FOUND);
            return result;
        }

        //update GameDeck records accordingly
        for (int i = 0; i < game.getPlayerCount(); i++) {
            List<GameDeck> gameDecks = gameDeckRepo.findByGameId(game.getGameId());
            for (GameDeck gd : gameDecks) {
                if (gd.getPosition() == i && gd.getDeckId() != game.getDecks()[i]) {
                    GameDeck gdToUpdate = gameDeckRepo.findById(gd.getGameDeckId());
                    gdToUpdate.setDeckId(game.getDecks()[i]);
                    if (!gameDeckRepo.update(gdToUpdate)) {
                        result.addMessage(String.format("Error updating game with id %s", game.getGameId()), ResultType.INVALID);
                        return result;
                    }
                }
            }
        }

        if (!gameRepo.update(game)) {
            result.addMessage(String.format("Error updating game id %s", game.getGameId()), ResultType.FAILURE);
        }

        return result;
    }

    public Result<Game> delete(int gameId) {
        Result<Game> result = new Result<>();
        List<GameDeck> gameDecks = gameDeckRepo.findByGameId(gameId);
        for (GameDeck gd: gameDecks) {
            if (!gameDeckRepo.deleteById(gd.getGameDeckId())) {
                result.addMessage("Failed to delete corresponding gameDeck row", ResultType.FAILURE);
                return result;
            }
        }

        if (!gameRepo.delete(gameId)) {
            result.addMessage(String.format("Failed to delete game with id %s", gameId), ResultType.FAILURE);
        }

        return result;
    }

    private Result<Game> validate(Game game) {
        Result<Game> result = new Result<>();

        if (game.getDatePlayed() == null) {
            result.addMessage("date played is required", ResultType.INVALID);
            return result;
        }

        if (game.getWinnerDeckId() == 0) {
            result.addMessage("winner is required", ResultType.INVALID);
            return result;
        }

        if (game.getPlayerCount() == 0) {
            result.addMessage("# of players is required", ResultType.INVALID);
            return result;
        }

        if (game.getDecks().length != game.getPlayerCount()) {
            result.addMessage("# decks must be equal to player count", ResultType.INVALID);
        }

        return result;
    }
}
