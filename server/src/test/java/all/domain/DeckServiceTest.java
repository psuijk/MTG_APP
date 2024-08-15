package all.domain;

import all.data.DeckRepository;
import all.data.GameDeckRepository;
import all.models.Deck;
import all.models.GameDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class DeckServiceTest {

    @Autowired
    DeckService service;

    @MockBean
    DeckRepository repository;

    @MockBean
    GameDeckRepository gameDeckRepository;

    @Test
    void shouldAddWhenValid() {
        Deck expected = makeDeck();
        Deck arg = makeDeck();
        arg.setDeckId(0);
        when(repository.add(arg)).thenReturn(expected);
        Result<Deck> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        //should not add when deckId is pre-set
        Deck deck = new Deck();
        Result<Deck> result = service.add(deck);
        assertEquals(ResultType.INVALID, result.getType());

        //should not add when player id is not set
        deck.setDeckId(0);
        deck.setPlayerId(0);
        result = service.add(deck);
        assertEquals(ResultType.INVALID, result.getType());

        //should not add when deck name is empty
        deck = makeDeck();
        deck.setName(null);
        result = service.add(deck);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdateWhenValid() {
        Deck deck = makeDeck();
        when(repository.findById(1)).thenReturn(deck);
        when(repository.update(deck)).thenReturn(true);
        Result<Deck> actual = service.update(deck);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        //should not update when deck not found
        Deck deck = makeDeck();
        when(repository.findById(1)).thenReturn(null);
        when(repository.update(deck)).thenReturn(false);
        Result<Deck> actual = service.update(deck);
        assertEquals(ResultType.NOT_FOUND, actual.getType());

        //should not update when player id is not set
        deck.setPlayerId(0);
        when(repository.findById(1)).thenReturn(deck);
        when(repository.update(deck)).thenReturn(false);
        actual = service.update(deck);
        assertEquals(ResultType.INVALID, actual.getType());

        //should not update when deck name is empty
        deck = makeDeck();
        deck.setName(null);
        when(repository.findById(1)).thenReturn(deck);
        when(repository.update(deck)).thenReturn(false);
        actual = service.update(deck);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldDelete() {
        when(gameDeckRepository.findByDeckId(1)).thenReturn(new ArrayList<GameDeck>());
        when(repository.deleteById(1)).thenReturn(true);
        when(repository.findById(1)).thenReturn(new Deck());
        Result<Deck> result = service.delete(1);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotDelete() {
        //deck does not exist
        Result<Deck> result = service.delete(1);
        assertEquals(ResultType.NOT_FOUND, result.getType());

        //deck is in use
        List<GameDeck> gameDecks = new ArrayList<>();
        gameDecks.add(new GameDeck(1, 1, 1, 1));
        gameDecks.add(new GameDeck(2, 1, 2, 2));
        gameDecks.add(new GameDeck(3, 1, 3, 3));
        gameDecks.add(new GameDeck(4, 1, 4, 4));
        when(gameDeckRepository.findByDeckId(1)).thenReturn(gameDecks);
        when(repository.findById(1)).thenReturn(new Deck());
        result = service.delete(1);
        assertEquals(ResultType.INVALID, result.getType());
    }

    private Deck makeDeck() {
        Deck deck = new Deck();
        deck.setDeckId(1);
        deck.setPlayerId(1);
        deck.setActive(true);
        deck.setName("My Krenko deck");
        deck.setCommanderId("Krenko");
        return deck;
    }
}