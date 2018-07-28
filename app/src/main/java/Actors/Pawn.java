package Actors;

public class Pawn {
    //int to define by how many (minor)pawns this one is formed
    //default:1
    private int capacity = 1;
    private final int pawnId;

    public Pawn(int id, int cap) {
        capacity = cap;
        pawnId = id;
    }

    public int getCap() {
        return capacity;
    }

    public int getId() {
        return pawnId;
    }

}
