package Actors;

public class Pawn {
    //int to define by how many (minor)pawns this one is formed
    //default:1
    private int capacity = 1;

    public Pawn(int cap) {
        capacity=cap;
    }

    public int getCap() {
        return capacity;
    }
}
