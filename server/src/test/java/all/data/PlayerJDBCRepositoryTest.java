package all.data;

import all.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlayerJDBCRepositoryTest {
    final static int NEXT_ID = 7;

    @Autowired
    PlayerJDBCRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void findAll() {
        List<Player> players = repository.findAll();
        assertNotNull(players);
        assertEquals(6, players.size());
    }

    @Test
    void findById() {
        Player player = repository.findById(1);
        assertEquals(1, player.getPlayerId());
        assertTrue(player.getFirstName().equalsIgnoreCase("Alice"));
    }

    @Test
    void add() {
        Player player = makePlayer();
        Player actual = repository.add(player);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getPlayerId());
    }

    @Test
    void update() {
        Player player = makePlayer();
        player.setPlayerId(1);
        assertTrue(repository.update(player));
        player.setPlayerId(9000);
        assertFalse(repository.update(player));
    }

    @Test
    void delete() {
        //Player player = repository.add(makePlayer());
        Player player = new Player(6, "Steve", "Vai");
        assertTrue(repository.delete(player));
        player = makePlayer();
        player.setPlayerId(9000);
        assertFalse(repository.delete(player));
    }

    private Player makePlayer() {
        Player player = new Player();
        player.setFirstName("John");
        player.setLastName("Smith");
        player.setUsername("john@smith.com");
        return player;
    }
}