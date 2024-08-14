package all.models;

public class GameDeck {
    private int gameDeckId;
    private int gameId;
    private int deckId;
    private int position;

    public GameDeck() {}

    public GameDeck(int gameDeckId, int gameId, int deckId, int position) {
        this.gameDeckId = gameDeckId;
        this.gameId = gameId;
        this.deckId = deckId;
        this.position = position;
    }

    public int getGameDeckId() {
        return gameDeckId;
    }

    public void setGameDeckId(int gameDeckId) {
        this.gameDeckId = gameDeckId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
