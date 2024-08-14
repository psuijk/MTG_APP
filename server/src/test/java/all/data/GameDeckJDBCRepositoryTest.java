package all.data;

import all.models.GameDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameDeckJDBCRepositoryTest {

    final static int NEXT_ID = 11;

    @Autowired
    GameDeckJDBCRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void findAll() {
        List<GameDeck> gameDeckList = repository.findAll();
        assertNotNull(gameDeckList);
        assertEquals(10, gameDeckList.size());
    }

    @Test
    void findById() {
        GameDeck gameDeck = repository.findById(1);
        assertEquals(1, gameDeck.getGameDeckId());
        assertEquals(1, gameDeck.getPosition());
    }

    @Test
    void add() {
        GameDeck gameDeck = makeGameDeck();
        GameDeck actual = repository.add(gameDeck);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getGameDeckId());
    }

    @Test
    void update() {
        GameDeck gameDeck = makeGameDeck();
        gameDeck.setGameDeckId(1);
        assertTrue(repository.update(gameDeck));
        gameDeck.setGameDeckId(9000);
        assertFalse(repository.update(gameDeck));
    }

    @Test
    void deleteById() {
        assertTrue(repository.deleteById(1));
        assertFalse(repository.deleteById(9000));
    }

    private GameDeck makeGameDeck() {
        GameDeck gameDeck = new GameDeck();
        gameDeck.setGameId(1);
        gameDeck.setDeckId(4);
        return gameDeck;
    }
}