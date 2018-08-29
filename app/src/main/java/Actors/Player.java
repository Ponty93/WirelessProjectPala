package Actors;
import java.lang.String;
import java.util.HashMap;
import java.util.UUID;
import java.util.ArrayList;
import Actors.Pawn;

public class Player {
    private final  int id;
    private final String userName;

    private int score;
    private HashMap<Integer,Pawn> pawns = new HashMap<>();
    //todo memorize the pawns
    /**
     * Builds a Player instance
     * @constructor
     */

    public Player(int idfromServer, String user) {
        id = idfromServer;
        userName = user;
        score = 0;
        buildMap();

    }
    private void buildMap() {
        for(int i=1;i<7;i++)
            pawns.put(i,new Pawn(i));

    }
    public int getUserId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Pawn getPawnbyId(int id){
        return pawns.get(id);
    }

    public int getScore(){
        return score;
    }

    public void setScore(int val){
        score = val;
    }

    public HashMap<Integer, Pawn> getPawns() {
        return pawns;
    }
    public void setPawnPosition(int id,int pos){
        pawns.get(id).setPosition(pos);

    }


    /**
     * Finds a pawn by passing his id
     * @param id
     * @return {Pawn}
     */
    //public Pawn findPawnById(int id)



}


