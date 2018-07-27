package GameLogic;



import GameLogic.Game;
import Actors.Player;

public class Board extends Game {
    private final int[] boardCells = new int[30];
    /* first dimension: #pawn
       second dimension : #cell in boardCells
       third dimension : # of pawn(of the same player) in the same cell
     */
    private int[][][] Player1Pawns = new int[6][30][1];
    private int[][][] Player2Pawns = new int[6][30][1];

    public void buildArray(int[][][] toBuild) {
        for(int i=0;i<toBuild.length;i++)
            for(int j=0;j<toBuild[i].length;j++)
            {
                toBuild[i][j][0]= 0; // sets the number of pawns to zero for each cell
            }

    }

    /**
     * create a Board instance to begin the game
     * @param playerOne
     * @param playerTwo
     */
    public Board(Player playerOne, Player playerTwo) {
        super(playerOne,playerTwo);
        buildArray(Player1Pawns);
        buildArray(Player2Pawns);
    }


}
