package all.data;

import all.models.GameDeck;

import java.util.List;

public interface GameDeckRepository {
    List<GameDeck> findAll();
    GameDeck findById(int gameDeckId);
    GameDeck add(GameDeck gameDeck);
    boolean update(GameDeck gameDeck);
    boolean deleteById(int gameDeckId);
}
