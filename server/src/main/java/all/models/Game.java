package all.models;

import java.time.LocalDate;

public class Game {
    private int gameId;
    private LocalDate datePlayed;
    private int winnerDeckId;
    private int playerCount;
    private int[] decks;

    public Game() {}

    public Game(int gameId, LocalDate datePlayed, int winnerDeckId, int playerCount, int[] decks) {
        this.gameId = gameId;
        this.datePlayed = datePlayed;
        this.winnerDeckId = winnerDeckId;
        this.playerCount = playerCount;
        this.decks = decks;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public LocalDate getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(LocalDate datePlayed) {
        this.datePlayed = datePlayed;
    }

    public int getWinnerDeckId() {
        return winnerDeckId;
    }

    public void setWinnerDeckId(int winnerDeckId) {
        this.winnerDeckId = winnerDeckId;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int[] getDecks() {
        return decks;
    }

    public void setDecks(int[] decks) {
        this.decks = decks;
    }
}
