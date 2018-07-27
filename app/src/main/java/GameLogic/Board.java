package GameLogic;



import java.util.HashMap;
import Actors.Pawn;
import GameLogic.Game;
import Actors.Player;

public class Board extends Game {

    private final HashMap<Integer,Pawn> player1Pawns = new HashMap<Integer,Pawn>();
    private final HashMap<Integer,Pawn> player2Pawns = new HashMap<Integer,Pawn>();

    /* first dimension: enum for #pawn
       second dimension : #cell in boardCells
     */
    private int[][] Pawns1Location = new int[6][30];
    private int[][] Pawns2Location = new int[6][30];

    public void buildMap(HashMap<Integer,Pawn> map) {
        for(int i=0;i<6;i++)
            map.put(i,new Pawn(1));
    }
    public void buildArray(int[][] toBuild) {
        for(int i=0;i<toBuild.length;i++)
            for(int j=0;j<toBuild[i].length;j++)
            {
                toBuild[i][j]= 0; // sets the number of pawns to zero for each cell
            }

    }

    /**
     * create a Board instance to begin the game
     * @param playerOne
     * @param playerTwo
     */
    public Board(Player playerOne, Player playerTwo) {
        super(playerOne,playerTwo);
        buildMap(player1Pawns);
        buildMap(player2Pawns);
        buildArray(Pawns1Location);
        buildArray(Pawns2Location);
    }

    /**
     * method to fuse two different pawns which were in the same cell
     * @param one
     * @param two
     * @param player
     * @return {Pawn}
     */
    public Pawn fusePawns(int one, int two, Player player) {
        Pawn aux;
        if(player.equals(getPlayer1())) {
            aux = new Pawn(player1Pawns.get(one).getCap()+player1Pawns.get(two).getCap());
            player1Pawns.remove(one);
            player1Pawns.remove(two);
            player1Pawns.put(one,aux);
        }
        else {
            aux = new Pawn(player2Pawns.get(one).getCap()+player2Pawns.get(two).getCap());
            player2Pawns.remove(one);
            player2Pawns.remove(two);
            player2Pawns.put(one,aux);
        }
        return aux;
    }



}
