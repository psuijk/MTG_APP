package all.data;

import all.models.Game;

import java.time.LocalDate;
import java.util.List;

public class GameRepositoryDouble implements GameRepository {
    @Override
    public List<Game> findAll() {
        return List.of();
    }

    @Override
    public Game findById(int gameId) {
        return new Game(1, LocalDate.of(1990, 1, 1), 1, 4, new int[] {1, 2, 3, 4});
    }

    @Override
    public Game add(Game game) {
        return game;
    }

    @Override
    public boolean update(Game game) {
        return true;
    }

    @Override
    public boolean delete(int gameId) {
        return true;
    }
}
