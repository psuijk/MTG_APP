package all.controllers;

import all.domain.DeckService;
import all.domain.Result;
import all.models.Deck;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/deck")
public class DeckController {
    private final DeckService service;

    public DeckController(DeckService service) { this.service = service; }

    @GetMapping
    public List<Deck> findAll() { return this.service.findAll(); }

    @GetMapping("/{deckId}")
    public Deck findById(@PathVariable int deckId) { return service.findById(deckId); }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Deck deck) {
        Result<Deck> result = service.add(deck);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @PutMapping("/{deckId}")
    public ResponseEntity<Object> update(@PathVariable int deckId, @RequestBody Deck deck) {
        if (deckId != deck.getDeckId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Deck> result = service.update(deck);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<Object> deleteById(@PathVariable int deckId) {
        Result<Deck> result = service.delete(deckId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }
}
