package Model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import API.phpConnect;
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
    private int numberOfMove = 0;

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
    public void roll() {

        diceBuffer[0] = (int) Math.random()*6+1;
        diceBuffer[1] = (int) Math.random()*6+1;

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

    public int getNumberOfMove(){
        return numberOfMove;
    }

    public void setNumberOfMove(int val){
        numberOfMove = val;
    }

    public void endTurn(){

        boolean serverResponse = false;
        try {
            phpConnect myConn = new phpConnect("https://psionofficial.com/Wireless/handler.php", getIdGame());
            myConn.execute("u", Integer.toString(getPlayer2().getUserId()), "endTurn", "-1", "-1").get();
        }catch(ExecutionException e){e.printStackTrace();}
        catch(InterruptedException e){e.printStackTrace();}

        if(serverResponse == true){
            //todo calls pawns upload board
        }

    }

    public JSONObject uploadBoard(){
        JSONObject board = new JSONObject();
        JSONArray player1 = new JSONArray();
        JSONArray player2 = new JSONArray();

        try {
            board.put("gameId", getIdGame());
            board.put("userNamePlayer1",getPlayer1().getUserName());
            board.put("idPlayer1",getPlayer1().getUserId());
            board.put("scorePlayer1",getPlayer1().getScore());
            board.put("userNamePlayer2",getPlayer2().getUserName());
            board.put("idPlayer2",getPlayer2().getUserId());
            board.put("scorePlayer2",getPlayer2().getScore());

            for (int i = 1; i < 7; i++) {
                JSONObject player2Pawns = new JSONObject();
                JSONObject player1Pawns = new JSONObject();
                player1Pawns.put("idPawn"+i,i);
                player1Pawns.put("pawnPosition",getPlayer1().getPawnbyId(i).getPosition());
                player2Pawns.put("idPawn"+i,i);
                player1.put(new JSONObject().put("pawn"+i,player1Pawns));
                player2Pawns.put("pawnPosition",getPlayer2().getPawnbyId(i).getPosition());
                player2.put(new JSONObject().put("pawn"+i,player2Pawns));
            }

            board.put("pawnsPlayer1",player1);
            board.put("pawnsPlayer2",player2);

            return board;
        }catch(JSONException e){e.printStackTrace();}

        return null;
    }

    public boolean updateRound(phpConnect connTimeout){
        try {
            return connTimeout.execute("r", "GAME", Integer.toString(getPlayer1().getUserId()), "round", "-1").get();
        }catch(InterruptedException e ){e.printStackTrace();}
        catch(ExecutionException e ){e.printStackTrace();}

        return false;
    }


    public boolean surrender(){
        phpConnect myConn = null;
        boolean result = false;
        try{
            myConn = new phpConnect("https://psionofficial.com/Wireless/handler.php",getIdGame());
            result = myConn.execute("u",Integer.toString(getPlayer2().getUserId()),"surrender","-1","-1").get();

        }catch(ExecutionException e){e.printStackTrace();}
        catch(InterruptedException e ){e.printStackTrace();}

        return result;

    }

    public int getDiceRes(int index){
        return diceBuffer[index];
    }

    public void setDiceResToNull(){
        diceBuffer[0]=0;
        diceBuffer[1]=0;
    }



}
