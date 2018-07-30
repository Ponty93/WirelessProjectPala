package Actors;
import java.lang.String;
import java.util.UUID;
import java.util.ArrayList;
import Actors.Pawn;

public class Player {
    private final  int id;
    private final String userName;
    private final boolean order;




    /**
     * Builds a Player instance
     * @constructor
     */
    public Player(boolean r,int idfromServer, String user) {
        id = idfromServer;
        userName = user;
        order = r;

    }

    public String getUserName() {
        return userName;
    }

    public boolean getOrder() {
        return order;
    }
    //public Pawn findPawnById(int id)

}
