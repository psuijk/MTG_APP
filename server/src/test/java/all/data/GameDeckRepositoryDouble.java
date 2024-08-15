package all.data;

import all.models.GameDeck;

import java.util.ArrayList;
import java.util.List;

public class GameDeckRepositoryDouble implements GameDeckRepository {
    List<GameDeck> gameDecks;
    public GameDeckRepositoryDouble() {
        gameDecks = new ArrayList<>();
        gameDecks.add(new GameDeck(1, 1, 1, 1));
        gameDecks.add(new GameDeck(2, 1, 2, 2));
        gameDecks.add(new GameDeck(3, 1, 3, 3));
        gameDecks.add(new GameDeck(4, 1, 4, 4));
    }

    @Override
    public List<GameDeck> findAll() {
        return gameDecks;
    }

    @Override
    public List<GameDeck> findByDeckId(int deckId) {
        List<GameDeck> result = new ArrayList<>();
        GameDeck gd = gameDecks.get(deckId -1);
        result.add(gd);
        return result;
    }

    @Override
    public List<GameDeck> findByGameId(int gameId) {
        return gameDecks;
    }

    @Override
    public GameDeck findById(int gameDeckId) {
        return gameDecks.get(gameDeckId - 1);
    }

    @Override
    public GameDeck add(GameDeck gameDeck) {
        return gameDeck;
    }

    @Override
    public boolean update(GameDeck gameDeck) {
        return true;
    }

    @Override
    public boolean deleteById(int gameDeckId) {
        return true;
    }
}
