package Actors;

public class Pawn {
    //int to define by how many (minor)pawns this one is formed
    //default:1

    private final int pawnId;

    public Pawn(int id, int cap) {

        pawnId = id;
    }


    public int getId() {
        return pawnId;
    }

}
