package GameLogic;



import java.util.HashMap;
import Actors.Pawn;
import GameLogic.Game;
import Actors.Player;

public class Board extends Game {
    /**
     * Position rules of thumb:
     *
     * -1 : position assigned to the pawns that are going to spawn
     * [0..29] : position in the board
     * 30 : position assigned to the winnerpawns
     */
    private int numberOfCell = 30;
    private int[] diceBuffer = new int[2];

    /**
     * create a Board instance to begin the game
     * @param cells define the number of cells the board has
     */
    public Board(int cells) {
        super();
        numberOfCell = cells;
    }

    /**
     * Assign two random values in sequence to the diceBuffer array
     * The method models two dice roll
     */
    public void diceRoll() {

        diceBuffer[0] = (int) Math.random()*6+1;
        diceBuffer[1] = (int) Math.random()*6+1;

    }

    /**
     * The method search for the pawns the user(WhoPlay) wants to move(could be just one)
     * @param v1
     * @param id1
     * @param v2
     * @param id2
     * v1 && id1 MUSTN'T be -1
     * v2 && id2 could be -1
     */
    /*public void planToMove(int v1, int id1, int v2, int id2) {
            movePawn(v1,findPlayerByUserName(whoPlay()).findPawnById(id1));
            if(id2 != id1)
                movePawn(v2,findPlayerByUserName(whoPlay()).findPawnById(id2));
            else
                movePawn(v2,findPlayerByUserName(whoPlay()).findPawnById(id1));
    }*/

    /**
     * Check if a pawn is able to win
     * @param id
     * @param val
     * @param player
     * @return {boolean}
     */
    //public boolean checkIfPawnWin(int id, int val, Player player)

    /**
     * Check if a pawn is going to be over the board size
     * @param val
     * @param id
     * @param player
     * @return {boolean}
     */
    //public boolean checkIfOutOfBoard(int id, int val, Player player)

    /**
     * Check if a pawn can eat a enemy pawn by checking the # of pawns in the board's cell
     * @param idPlayer1
     * @param idPlayer2
     * @return {boolean}
     */

    //public boolean canEat(int idPlayer1,int idPlayer2)

    /**
     * Sets the position value of a enemy pawn that can be eaten to -1
     * @param id
     * @param player
     * @return {void}
     */
    //public void eatPawn(int id,Player player)

    /**
     * Move a pawn according to the values passed as param
     * The method also check if the pawn is at the end line or is over that
     * @param val
     * @param pawn
     */
    //public void movePawn(int val, Pawn pawn)





}
