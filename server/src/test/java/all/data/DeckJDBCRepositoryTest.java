package all.data;

import all.models.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeckJDBCRepositoryTest {
    final static int NEXT_ID = 6;
    @Autowired
    DeckJDBCRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void findAll() {
        List<Deck> decks = repository.findAll();
        assertNotNull(decks);
        assertEquals(5, decks.size());
    }

    @Test
    void findById() {
        Deck deck = repository.findById(1);
        assertEquals(1, deck.getDeckId());
        assertTrue(deck.getName().equalsIgnoreCase("Dragons Fury"));
    }

    @Test
    void add() {
        Deck deck = makeDeck();
        Deck actual = repository.add(deck);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getDeckId());
    }

    @Test
    void update() {
        Deck deck = makeDeck();
        deck.setDeckId(1);
        assertTrue(repository.update(deck));
        deck.setDeckId(9000);
        assertFalse(repository.update(deck));
    }

    @Test
    void deleteById() {
        Deck deck = repository.add(makeDeck());
        assertTrue(repository.deleteById(deck.getDeckId()));
        assertFalse(repository.deleteById(9000));
    }

    private Deck makeDeck() {
        Deck deck = new Deck();
        deck.setName("Blue Shenanigans");
        deck.setPlayerId(1);
        deck.setActive(true);
        deck.setCommanderId("Krenko");
        return deck;
    }
}