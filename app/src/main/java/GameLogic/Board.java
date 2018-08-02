package GameLogic;
import API.phpConnect;
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
    private phpConnect connectionHandler = new phpConnect(new String(),getIdGame());
    /**
     * create a BoardActivity instance to begin the game
     * @param cells define the number of cells the board has
     */
    public Board(int cells) {
        super();
        numberOfCell = cells;
    }

    public Board(int cells,Player player1, Player player2) {
        super(player1,player2);
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
     * Method who organize how a user wants to move his pawns
     * @param v1
     * @param id1
     * @param v2
     * @param id2
     * @param player
     */

    public void howToMove(int v1,int id1,int v2,int id2, Player player) {
        if(id1 != id2) {
            planToMove(v1,id1,player);
            planToMove(v2,id2,player);
        }
        else
            planToMove(v1+v2,id1,player);
    }

    /**
     * The method search for the pawns the user wants to move
     * @param val
     * @param id
     * @param player
     */
    public void planToMove(int val, int id, Player player) {
        getConnection().execute("r",Integer.toString(player.getUserId()),Integer.toString(id));
        int posToStart = Integer.parseInt(getConnection().getParamFromJson("position"));
        int posToArrive = posToStart+ val;
        if(posToArrive < numberOfCell) {
                if(checkIfPawnWin(posToArrive,player,id))
                    return;
                else {
                    if(canEat(player.getUserId(),findPlayerByRemoving(player.getUserId()).getUserId(),posToStart,posToArrive))
                        eatPawn(posToArrive,findPlayerByRemoving(player.getUserId()));
                    else
                        movePawn(posToArrive,id,player);
                }
            }
    }

    /**
     * Check if a pawn is able to win
     * @param posToArrive
     * @return {boolean}
     */
    //todo replace string URL
    public boolean checkIfPawnWin(int posToArrive, Player player, int id) {
        if(posToArrive == numberOfCell-1) {
            getConnection().execute("u",Integer.toString(player.getUserId()),"pawnId",Integer.toString(id),"position",Integer.toString(posToArrive));
            return true;
        }
        else
            return false;
    }


    /**
     * Check if a pawn can eat a enemy pawn by checking the # of pawns in the board's cell
     * @param idPlayer1
     * @param idPlayer2
     * @param posToEnd
     * @return {boolean}
     */

    public boolean canEat(int idPlayer1,int idPlayer2,int posToStart,int posToEnd) {

        int countPlayer1 = howManyPawns(posToStart, idPlayer1);
        int countPlayer2 = howManyPawns(posToEnd,idPlayer2);
        if(countPlayer1>=countPlayer2)
            return true;
        else
            return false;
    }

    /**
     * Sets the position value of a enemy pawn that can be eaten to -1
     * @param pos
     * @param player
     * @return {void}
     */
    public void eatPawn(int pos,Player player) {
        getConnection().execute("u",Integer.toString(player.getUserId()),"positionToUpdate",Integer.toString(pos),"positionUpdated",Integer.toString(-1));
    }

    /**
     * Move a pawn according to the values passed as param
     * The method also check if the pawn is at the end line or is over that
     * @param val
     * @param pawnId
     */
    public void movePawn(int val, int pawnId, Player player) {
        getConnection().execute("u", Integer.toString(player.getUserId()),"pawnId", Integer.toString(pawnId),"position",Integer.toString(val));
    }


    /**
     * Asks the capacity of a specified position to the db
     * @param pos
     * @return {int}
     */
    public int howManyPawns(int pos, int playerId)
    {
        getConnection().execute("r",Integer.toString(playerId),"pos",Integer.toString(pos));
        return Integer.parseInt(getConnection().getParamFromJson("count"));
    }


    public phpConnect getConnection() {
        return connectionHandler;
    }



}
