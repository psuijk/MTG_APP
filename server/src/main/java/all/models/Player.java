package all.models;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int playerId;
    private String username = null;
    private String firstName;
    private String lastName;
    private List<Deck> playerDecks;

    public Player() {}

    public Player(int playerId, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.playerId = playerId;
        this.playerDecks = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public List<Deck> getPlayerDecks() {
        return playerDecks;
    }

    public void setPlayerDecks(List<Deck> playerDecks) {
        this.playerDecks = playerDecks;
    }
}
