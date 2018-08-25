package Actors;

import android.content.Context;
import android.widget.ImageView;

public class Pawn {
    //int to define by how many (minor)pawns this one is formed
    //default:1
    ImageView pawn = null;
    private final int pawnId;
    private int position = -1;
    private int cap = 1 ;

    public Pawn(int id) {

        pawnId = id;
    }

    public void setPawnView(ImageView pawnView){
        pawn = pawnView ;
    }
    public int getId() {
        return pawnId;
    }
    public int getPosition() {
        return position;
    }

}
