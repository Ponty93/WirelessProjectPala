package Actors;
import java.lang.String;
import java.util.UUID;
import java.util.ArrayList;
import Actors.Pawn;

public class Player {
    private final  UUID id;
    private final String userName;
    private final ArrayList<Pawn> playerPawns = new ArrayList<>();
    private final boolean order;


    public void buildList(ArrayList<Pawn> arr) {
        for(int i=0;i<6;i++)
            arr.add(0,new Pawn(i,1));
    }

    /**
     * Builds a Player instance
     * @constructor
     */
    public Player(boolean r) {
        id = new UUID(16,48);//todo
        userName = "samplePlayer";
        order = r;
        buildList(playerPawns);
    }

    public String getUserName() {
        return userName;
    }

    public boolean getOrder() {
        return order;
    }
    public Pawn findPawnById(int id) {
        return playerPawns.get(id);
    }

}
