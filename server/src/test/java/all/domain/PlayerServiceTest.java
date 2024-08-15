package all.domain;

import all.data.PlayerRepository;
import all.models.Deck;
import all.models.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PlayerServiceTest {

    @Autowired
    PlayerService service;

    @MockBean
    PlayerRepository repository;

    @Test
    void shouldAddWhenValid() {
        Player expected = makePlayer();
        Player arg = makePlayer();
        arg.setPlayerId(0);
        when(repository.add(arg)).thenReturn(expected);
        Result<Player> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        //player id pre set
        Player player = makePlayer();
        Result<Player> result = service.add(player);
        assertEquals(ResultType.INVALID, result.getType());
        //first name empty or null
        player.setPlayerId(0);
        player.setFirstName("");
        result = service.add(player);
        assertEquals(ResultType.INVALID, result.getType());
        //last name empty or null
        player.setFirstName("Bob");
        player.setLastName("");
        result = service.add(player);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdateWhenValid() {
        Player player = makePlayer();
        when(repository.findById(1)).thenReturn(player);
        when(repository.update(player)).thenReturn(true);
        Result<Player> result = service.update(player);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void ShouldNotUpdateWhenInvalid() {
        //player id not set
        Player player = makePlayer();
        player.setPlayerId(0);
        Result<Player> result = service.update(player);
        assertEquals(ResultType.NOT_FOUND, result.getType());
        //first name empty or null
        when(repository.findById(1)).thenReturn(player);
        player = makePlayer();
        player.setFirstName("");
        result = service.update(player);
        assertEquals(ResultType.INVALID, result.getType());
        //last name empty or null
        player = makePlayer();
        player.setLastName("");
        result = service.update(player);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldDelete() {
        Player player = makePlayer();
        when(repository.findById(player.getPlayerId())).thenReturn(player);
        when(repository.delete(player)).thenReturn(true);
        Result<Player> result = service.delete(player);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotDelete() {
        //username not null
        Player player = makePlayer();
        when(repository.findById(player.getPlayerId())).thenReturn(player);
        player.setUsername("abc");
        Result<Player> result = service.delete(player);
        assertEquals(ResultType.INVALID, result.getType());

        //player with existing decks
        player = makePlayer();
        List<Deck> decks = new ArrayList<>();
        decks.add(new Deck());
        player.setPlayerDecks(decks);
        result = service.delete(player);
        assertEquals(ResultType.INVALID, result.getType());

        //player not found
        player = makePlayer();
        when(repository.findById(player.getPlayerId())).thenReturn(null);
        result = service.delete(player);
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    private Player makePlayer() {
        Player player = new Player();
        player.setPlayerId(1);
        player.setFirstName("Bob");
        player.setLastName("Odenkirk");
        player.setPlayerDecks(new ArrayList<>());
        return player;
    }
}