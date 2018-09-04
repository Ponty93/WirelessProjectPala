package Model;
import android.util.Log;

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
        setDiceResToNull();
    }
    /**
     * Assign two random values in sequence to the diceBuffer array
     * The method models two dice roll
     */
    public void roll() {
        diceBuffer[0] = (int) (Math.random()*6+1);
        String aux = "tempo da far saltare";
        diceBuffer[1] = (int) (Math.random()*6+1);

    }


    /**
     * Check if a pawn is able to win
     * @param playerId
     * @param posToStart
     * @param posToArrive
     * @return {boolean}
     */
    public boolean checkIfPawnWin(int playerId,int posToStart, int posToArrive) {
        if(posToArrive == numberOfCell) {
            phpConnect conn = new phpConnect("https://www.psionofficial.com/Wireless/handler.php", getIdGame());
            try {
                if(conn.execute("u",Integer.toString(playerId),"position",Integer.toString(posToStart),Integer.toString(posToArrive)).get())
                    return true;

            }catch(ExecutionException e){
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return false;
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
        phpConnect conn = new phpConnect("https://www.psionofficial.com/Wireless/handler.php", getIdGame());
        try {
            conn.execute("u",Integer.toString(playerId),"position",Integer.toString(posToStart),"0").get();

        }catch(ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

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

        phpConnect conn = new phpConnect("https://www.psionofficial.com/Wireless/handler.php", getIdGame());
        try {
            if(posToStart == 0)
                conn.execute("u",Integer.toString(playerId),"pawnId",Integer.toString(pawnId),Integer.toString(posToArrive)).get();
            else
                conn.execute("u",Integer.toString(playerId),"position",Integer.toString(posToStart),Integer.toString(posToArrive)).get();
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }


    }


    /**
     * Asks the capacity of a specified position to the db
     * @param pos
     * @param playerId :
     * @return {int}
     */

    //todo redefine this method as a JSonObject that contains:
    // - the number of pawns im that pos
    // - the id of the pawns in that pos
    public int howManyPawns(int pos, int playerId)
    {   phpConnect conn = new phpConnect("https://www.psionofficial.com/Wireless/handler.php", getIdGame());
        try {
            if (conn.execute("r", "PAWN", Integer.toString(playerId), "pos", Integer.toString(pos)).get())
                return Integer.parseInt(conn.getParamFromJson("count"));
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return 0;
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
            phpConnect myConn = new phpConnect("https://psionofficial.com/Wireless/try.php", getIdGame());
            myConn.execute("u", "-1", "boardUpdate", "-1", "-1",uploadBoard().toString()).get();
            phpConnect myConn2 = new phpConnect("https://psionofficial.com/Wireless/handler.php", getIdGame());
            myConn2.execute("u", Integer.toString(getPlayer2().getUserId()), "endTurn", "-1", "-1","-1").get();
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
            board.put("roundNumber",getRoundsCounter());

            for (int i = 1; i < 7; i++) {
                JSONObject player2Pawns = new JSONObject();
                JSONObject player1Pawns = new JSONObject();
                player1Pawns.put("idPawn"+i,getPlayer1().getPawnbyId(i).getIdDB());
                player1Pawns.put("pawnPosition",getPlayer1().getPawnbyId(i).getPosition());
                player2Pawns.put("idPawn"+i,getPlayer2().getPawnbyId(i).getIdDB());
                player2Pawns.put("pawnPosition",getPlayer2().getPawnbyId(i).getPosition());
                player1.put(new JSONObject().put("pawn"+i,player1Pawns));
                player2.put(new JSONObject().put("pawn"+i,player2Pawns));
            }

            board.put("pawnsPlayer1",player1);
            board.put("pawnsPlayer2",player2);

            return board;
        }catch(JSONException e){e.printStackTrace();}

        return null;
    }

    public boolean updateBoard(JSONObject json){
        try{
            //Log.d("GAMEID","IS"+json.getInt("gameId")+"   "+"LOCAL GAME ID"+getIdGame());
            if(getIdGame() == json.getInt("gameId")){
                JSONObject player1 = json.getJSONObject("pawnPlayer1");
                JSONObject player2 = json.getJSONObject("pawnPlayer2");
                if(player1.getInt("ID")==getPlayer1().getUserId()){
                    if(player2.getInt("ID")== getPlayer2().getUserId()) {
                        for (int i = 1; i < 7; i++) {
                            getPlayer1().getPawnbyId(i).setPosition(player1.getInt("pawnPos" + i));
                            getPlayer1().getPawnbyId(i).setPawnIdFromDb(player1.getInt("pawnId" + i));
                            getPlayer2().getPawnbyId(i).setPosition(player2.getInt("pawnPos" + i));
                            getPlayer2().getPawnbyId(i).setPawnIdFromDb(player2.getInt("pawnId" + i));
                        }
                        return true;
                    }
                    else
                        return false;
                }
                else if(player2.getInt("ID")== getPlayer1().getUserId()){
                    if(player1.getInt("ID")==getPlayer2().getUserId()) {
                        for (int i = 1; i < 7; i++) {
                            getPlayer2().getPawnbyId(i).setPosition(player1.getInt("pawnPos" + i));
                            getPlayer2().getPawnbyId(i).setPawnIdFromDb(player1.getInt("pawnId" + i));
                            getPlayer1().getPawnbyId(i).setPosition(player2.getInt("pawnPos" + i));
                            getPlayer1().getPawnbyId(i).setPawnIdFromDb(player2.getInt("pawnId" + i));
                        }
                        return true;
                    }
                    else
                        return false;
                }
                else
                    return false;
            }

        }catch(JSONException e){e.printStackTrace();}
        return false;
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
            result = myConn.execute("u",Integer.toString(getPlayer2().getUserId()),"surrender","-1","-1","-1").get();

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
    public void setDiceResToNullInPos(int index){
        diceBuffer[index]=0;
    }


}
