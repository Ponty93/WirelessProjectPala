package Actors;

import android.content.Context;
import android.media.Image;
import android.widget.ImageView;

public class Pawn {
    //int to define by how many (minor)pawns this one is formed
    //default:1
    ImageView pawn = null;
    private final int pawnId;
    private int pawnIdFromDb;
    private int position = 0; //position leggends:
                                // 1 : Start
                                // 31 : Finish
                                // -1 : currently in a stack
    private int cap = 1 ;

    public Pawn(int id,int pawnDb) {
        pawnIdFromDb = pawnDb;
        pawnId = id;
    }

    public void setPawnView(ImageView pawnView){
        pawn = pawnView ;
    }

    public ImageView getPawn() {
        return pawn;
    }
    public int getIdDB(){return pawnIdFromDb;}
    public void setPawnIdFromDb(int id){pawnIdFromDb=id;}
    public int getId() {
        return pawnId;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int pos){position = pos;}

}
