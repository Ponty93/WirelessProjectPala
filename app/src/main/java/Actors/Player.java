package Actors;
import java.lang.String;
import java.util.HashMap;
import java.util.UUID;
import java.util.ArrayList;
import Actors.Pawn;

public class Player {
    private final  int id;
    private final String userName;
    private final boolean order;
    private int score;
    private HashMap<Integer,Pawn> pawns = new HashMap<>();
    //todo memorize the pawns
    /**
     * Builds a Player instance
     * @constructor
     */

    public Player(boolean r,int idfromServer, String user) {
        id = idfromServer;
        userName = user;
        order = r;
        score = 0;
        buildMap();

    }
    private void buildMap() {
        for(int i=0;i<6;i++)
            pawns.put(i,new Pawn(i));

    }
    public int getUserId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public boolean getOrder() {
        return order;
    }

    public Pawn getPawnbyId(int id){
        return pawns.get(id);
    }

    public HashMap<Integer, Pawn> getPawns() {
        return pawns;
    }


    /**
     * Finds a pawn by passing his id
     * @param id
     * @return {Pawn}
     */
    //public Pawn findPawnById(int id)



}


