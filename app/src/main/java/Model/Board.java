package Model;
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
     * create a BoardActivity instance to begin the game
     * @param cells define the number of cells the board has
     * @param player1
     * @param player2
     * @param gameId
     */
    public Board(int cells,Player player1, Player player2,int gameId) {
        super(player1,player2,gameId);
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
        getConnection().execute("r","PAWN",Integer.toString(player.getUserId()),Integer.toString(id));
        int posToStart = Integer.parseInt(getConnection().getParamFromJson("position"));
        int posToArrive = posToStart+ val;
        if(posToArrive < numberOfCell) {
                if(checkIfPawnWin(player.getUserId(),posToArrive,posToStart))
                    return;
                else {
                    if(canEat(player.getUserId(),findPlayerByRemoving(player.getUserId()).getUserId(),posToStart,posToArrive)) {
                        eatPawn(player.getUserId(),posToArrive);
                        movePawn(player.getUserId(),id,posToStart,posToArrive);
                    }
                    else
                        movePawn(player.getUserId(),id,posToStart,posToArrive);
                }
            }
            /*else {
            //shows a popup to inform the user the move is not valid
        }*/
    }

    /**
     * Check if a pawn is able to win
     * @param playerId
     * @param posToStart
     * @param posToArrive
     * @return {boolean}
     */
    public boolean checkIfPawnWin(int playerId,int posToStart, int posToArrive) {
        if(posToArrive == numberOfCell-1) {
            getConnection().execute("u",Integer.toString(playerId),"position",Integer.toString(posToStart),Integer.toString(posToArrive));
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
     * @param posToStart
     * @param playerId
     * @return {void}
     */
    public void eatPawn(int playerId,int posToStart) {
        getConnection().execute("u",Integer.toString(playerId),"position",Integer.toString(posToStart),Integer.toString(-1));
    }

    /**
     * Move a pawn according to the values passed as param
     * The method also check if the pawn is at the end line or is over that
     * @param playerId
     * @param pawnId
     * @param posToStart
     * @param posToArrive
     * @return {void}
     */
    public void movePawn(int playerId,int pawnId,int posToStart, int posToArrive) {
        if(posToStart == -1)
            getConnection().execute("u",Integer.toString(playerId),"pawnId",Integer.toString(pawnId),Integer.toString(posToArrive));
        else
            getConnection().execute("u",Integer.toString(playerId),"position",Integer.toString(posToStart),Integer.toString(posToArrive));
    }


    /**
     * Asks the capacity of a specified position to the db
     * @param pos
     * @return {int}
     */
    public int howManyPawns(int pos, int playerId)
    {
        getConnection().execute("r","PAWN",Integer.toString(playerId),"pos",Integer.toString(pos));
        return Integer.parseInt(getConnection().getParamFromJson("count"));
    }






}
