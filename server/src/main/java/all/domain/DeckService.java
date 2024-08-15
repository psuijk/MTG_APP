package all.domain;

import all.data.DeckJDBCRepository;
import all.data.DeckRepository;
import all.data.GameDeckRepository;
import all.models.Deck;
import all.models.GameDeck;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {

    DeckRepository deckRepo;
    GameDeckRepository gameDeckRepo;

    public DeckService (DeckRepository deckRepo, GameDeckRepository gameDeckRepo) {
        this.deckRepo = deckRepo;
        this.gameDeckRepo = gameDeckRepo;
    }

    public List<Deck> findAll() { return deckRepo.findAll(); }

    public Deck findById(int deckId) { return deckRepo.findById(deckId); }

    public Result<Deck> add(Deck deck) {
        Result<Deck> result = validate(deck);

        if (!result.isSuccess()) {
            return result;
        }

        if (deck.getDeckId() != 0) {
            result.addMessage("deck id cannot be set for add operation", ResultType.INVALID);
            return result;
        }

        deck = deckRepo.add(deck);
        result.setPayload(deck);

        return result;

    }

    public Result<Deck> update(Deck deck) {
        Result<Deck> result = validate(deck);

        if (!result.isSuccess()) {
            return result;
        }

        Deck check = deckRepo.findById(deck.getDeckId());

        if (check == null) {
            result.addMessage(String.format("deckId: %s not found", deck.getDeckId()), ResultType.NOT_FOUND);
            return result;
        }

        if (!deckRepo.update(deck)) {
            result.addMessage(String.format("Error updating deckId %s", deck.getDeckId()), ResultType.FAILURE);
        }

        return result;
    }

    public Result<Deck> delete(int deckId) {
        Result<Deck> result = new Result<>();
        if (deckRepo.findById(deckId) == null) {
            result.addMessage(String.format("deck id %s not found", deckId), ResultType.NOT_FOUND);
            return result;
        }

        List<GameDeck> gameDecks = gameDeckRepo.findByDeckId(deckId);
        if (!gameDecks.isEmpty()) {
            result.addMessage("cannot delete deck that is part of an existing game", ResultType.INVALID);
            return result;
        }

        if (!deckRepo.deleteById(deckId)) {
            result.addMessage(String.format("Error deleting deck %s", deckId), ResultType.FAILURE);
        }

        return result;
    }

    private Result<Deck> validate(Deck deck) {
        Result<Deck> result = new Result<>();

        if (deck.getPlayerId() == 0) {
            result.addMessage("player id is required", ResultType.INVALID);
            return result;
        }

        if (deck.getName() == null || deck.getName().isEmpty()) {
            result.addMessage("deck name is required", ResultType.INVALID);
            return result;
        }

        return result;
    }
}
