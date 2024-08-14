package all.data;

import all.models.Deck;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DeckRepository {
    List<Deck> findAll();
    Deck findById(int deckId);
    //findByPlayerId?
    Deck add(Deck deck);
    boolean update(Deck deck);
    @Transactional
    boolean deleteById(int deckId);
}
