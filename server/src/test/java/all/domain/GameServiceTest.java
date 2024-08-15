package all.domain;

import all.data.GameDeckRepository;
import all.data.GameDeckRepositoryDouble;
import all.data.GameRepository;
import all.data.GameRepositoryDouble;
import all.models.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest
class GameServiceTest {

    GameService service;

    @BeforeEach
    void setup() {
        service = new GameService(new GameDeckRepositoryDouble(), new GameRepositoryDouble());
    }

    @Test
    void findByDeckId() {
        //to do?
    }

    @Test
    void findByPlayer() {
        //to do?
    }

    @Test
    void shouldAddWhenValid() {
        Game game = makeGame();
        Result<Game> result = service.add(game);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        //game id pre set
        Game game = makeGame();
        game.setGameId(1);
        Result<Game> result = service.add(game);
        assertEquals(ResultType.INVALID, result.getType());
        //date played null
        game = makeGame();
        game.setDatePlayed(null);
        result = service.add(game);
        assertEquals(ResultType.INVALID, result.getType());
        //winner deck id not set
        game = makeGame();
        game.setWinnerDeckId(0);
        result = service.add(game);
        assertEquals(ResultType.INVALID, result.getType());
        //player count not set
        game = makeGame();
        game.setPlayerCount(0);
        result = service.add(game);
        assertEquals(ResultType.INVALID, result.getType());
        //decks not set // #decks != playercount
        game = makeGame();
        game.setDecks(new int[]{});
        result = service.add(game);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdateWhenValid() {
        Game game = makeGame();
        game.setGameId(1);
        Result<Game> result = service.update(game);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        //date played null
        Game game = makeGame();
        game.setGameId(1);
        game.setDatePlayed(null);
        Result<Game> result = service.update(game);
        assertEquals(ResultType.INVALID, result.getType());
        //winner deck id not set
        game = makeGame();
        game.setGameId(1);
        game.setWinnerDeckId(0);
        result = service.update(game);
        assertEquals(ResultType.INVALID, result.getType());
        //player count not set
        game = makeGame();
        game.setGameId(1);
        game.setPlayerCount(0);
        result = service.update(game);
        assertEquals(ResultType.INVALID, result.getType());
        //decks not set // #decks != player count
        game = makeGame();
        game.setGameId(1);
        game.setDecks(new int[]{});
        result = service.update(game);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldDelete() {
        //Game game = makeGame();
        Result<Game> result = service.delete(1);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    private Game makeGame() {
        Game game = new Game();
        game.setGameId(0);
        game.setDatePlayed(LocalDate.of(1990, 1, 1));
        game.setWinnerDeckId(1);
        game.setPlayerCount(4);
        game.setDecks(new int[] {1, 2, 3, 4});
        return game;
    }
}