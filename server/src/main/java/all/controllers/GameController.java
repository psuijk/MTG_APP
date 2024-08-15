package all.controllers;

import all.domain.GameService;
import all.domain.Result;
import all.models.Deck;
import all.models.Game;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService service;

    public GameController(GameService service) { this.service = service; }

    @GetMapping
    public List<Game> findAll() { return service.findAll(); }

    @GetMapping("/{gameId}")
    public Game findById(@PathVariable int gameId) { return service.findById(gameId); }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Game game) {
        Result<Game> result = service.add(game);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<Object> update(@PathVariable int gameId, @RequestBody Game game) {
        if (gameId != game.getGameId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Game> result = service.update(game);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Object> deleteById(@PathVariable int gameId) {
        Result<Game> result = service.delete(gameId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

}
