package all.controllers;

import all.domain.DeckService;
import all.domain.PlayerService;
import all.domain.Result;
import all.models.Deck;
import all.models.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    private final PlayerService service;

    public PlayerController(PlayerService service) { this.service = service; }

    @GetMapping
    public List<Player> findAll() { return this.service.findAll(); }

    @GetMapping("/{playerId}")
    public Player findById(@PathVariable int playerId) { return service.findById(playerId); }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Player player) {
        Result<Player> result = service.add(player);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<Object> update(@PathVariable int playerId, @RequestBody Player player) {
        if (playerId != player.getPlayerId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Player> result = service.update(player);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Player player) {
        Result<Player> result = service.delete(player);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }
}
