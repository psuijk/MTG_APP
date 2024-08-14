package all.models;

public class Deck {
    private int deckId;
    private int playerId;
    private String name;
    private boolean active;
    private String commanderId;

    public Deck() {}

    public Deck(int deckId, int playerId, String name, boolean active, String commanderId) {
        this.deckId = deckId;
        this.playerId = playerId;
        this.name = name;
        this.active = active;
        this.commanderId = commanderId;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCommanderId() {
        return commanderId;
    }

    public void setCommanderId(String commanderId) {
        this.commanderId = commanderId;
    }
}
