package Actors;
import java.lang.String;
import java.util.UUID;

public class Player {
    private final  UUID id;
    private final String playerName;

    /**
     * Builds a Player instance
     * @constructor
     */
    public Player() {
        id = new UUID(16,48);//todo
        playerName = "samplePlayer";
    }

}
