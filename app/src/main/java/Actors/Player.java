package Actors;
import java.lang.String;
import java.util.UUID;
import java.util.ArrayList;
import Actors.Pawn;

public class Player {
    private final  int id;
    private final String userName;
    private final boolean order;
    private int score;


    /**
     * Builds a Player instance
     * @constructor
     */
    public Player() {
        id=123;
        userName="dummyPlayer";
        order = false;
        score = 0;

    }
    public Player(boolean r,int idfromServer, String user) {
        id = idfromServer;
        userName = user;
        order = r;
        score = 0;

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

    /**
     * Finds a pawn by passing his id
     * @param id
     * @return {Pawn}
     */
    //public Pawn findPawnById(int id)



}


