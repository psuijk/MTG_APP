package all.data;

import all.models.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameJDBCRepositoryTest {

    final static int NEXT_ID = 7;

    @Autowired
    GameJDBCRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void findAll() {
        List<Game> games = repository.findAll();
        assertNotNull(games);
        assertTrue(games.size() == 6);
    }

    @Test
    void findById() {
        Game game = repository.findById(1);
        assertEquals(1, game.getGameId());
        assertEquals(4, game.getPlayerCount());
        assertEquals(1, game.getWinnerDeckId());
    }

    @Test
    void add() {
        Game game = makeGame();
        Game actual = repository.add(game);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getGameId());
    }

    @Test
    void update() {
        Game game = makeGame();
        game.setGameId(1);
        assertTrue(repository.update(game));
        game.setGameId(9000);
        assertFalse(repository.update(game));
    }

    @Test
    void delete() {
        //Game game = new Game(6, LocalDate.of(2024, 8, 5), 5, 4, new int[] {1, 2, 3, 4});
        assertTrue(repository.delete(6));
        //game.setGameId(9000);
        assertFalse(repository.delete(9000));
    }

    private Game makeGame() {
        Game game = new Game();
        game.setDatePlayed(LocalDate.of(1990, 1, 1));
        game.setWinnerDeckId(4);
        game.setPlayerCount(4);
        game.setDecks(new int[] {1, 2, 3, 4});
        return game;
    }
}