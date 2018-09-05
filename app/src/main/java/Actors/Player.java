package Actors;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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

    public Player(int idfromServer, String user, JSONObject pawnId) {
        id = idfromServer;
        userName = user;
        score = 0;
        buildMap(pawnId);

    }
    private void buildMap(JSONObject json) {
        Log.d("costruisco hashmap","qui");
        try {
            for (int i = 1; i < 7; i++) {
                pawns.put(i, new Pawn(i, json.getInt("pawnId" + i)));
            }
        }catch(JSONException e){e.printStackTrace();}
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
    public Pawn findPawnByPos(int pos){
        for(int i=1;i<7;i++){
            if(getPawnbyId(i).getPosition() == pos)
                return getPawnbyId(i);
        }
        return null;
    }


    /**
     * Finds a pawn by passing his id
     * @param id
     * @return {Pawn}
     */
    //public Pawn findPawnById(int id)



}


