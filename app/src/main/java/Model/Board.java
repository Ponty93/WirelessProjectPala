package Model;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import API.phpConnect;
import Actors.Player;
import Utils.dice;

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
    private dice diceRes;

    /**
     * create a BoardActivity instance to begin the game
     * @param cells define the number of cells the board has
     * @param player1
     * @param player2
     * @param gameId
     */
    public Board(int cells,Player player1, Player player2,int gameId,dice d) {
        super(player1,player2,gameId);
        numberOfCell = cells;
        diceRes = d;
        diceRes.setDiceResToNull();
    }



    /**
     * Check if a pawn is able to win
     * @param posToArrive
     * @return {boolean}
     */
    public boolean checkIfPawnWin(int posToArrive) {
        if(posToArrive == numberOfCell)
           return true;

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
     * @param id
     * @return {void}
     */
    public void eatPawn(int id) {
        getPlayer2().getPawnbyId(id).setPosition(0);

    }

    /**
     * Move a pawn according to the values passed as param
     * @return {void}
     */
    public void movePawn(int idPawn,int posNewParent) {
        getPlayer1().setPawnPosition(idPawn,posNewParent);
    }


    /**
     * Asks the capacity of a specified position to the db
     * @param pos
     * @param playerId :
     * @return {int}
     */

    public int howManyPawns(int pos, int playerId)
    {
        int counter = 0;
        int index = 1;
        if(playerId == getPlayer1().getUserId()) {
            while (index < 7) {
                if (getPlayer1().getPawnbyId(index).getPosition() == pos)
                    counter++;
                index++;
            }
        }
        else{
            while (index < 7) {
                if (getPlayer2().getPawnbyId(index).getPosition() == pos)
                    counter++;
                index++;
            }
        }
        return counter;
    }

    public int getNumberOfMove(){
        return numberOfMove;
    }

    public void setNumberOfMove(int val){
        numberOfMove = val;
    }

    public boolean endTurn(){
        try {
            if(uploadBoard() == true){
                phpConnect myConn2 = new phpConnect("https://psionofficial.com/Wireless/handler.php", getIdGame());
                return myConn2.execute("u", Integer.toString(getPlayer2().getUserId()), "endTurn", "-1", "-1", "-1").get();
            }
        }catch(ExecutionException e){e.printStackTrace();}
        catch(InterruptedException e){e.printStackTrace();}
        return false;
    }


    public boolean uploadBoard(){
        try{
            JSONObject json = buildBoardJSON();
            //Log.d("JSON UPLOAD","IS"+json);
            phpConnect conn = new phpConnect("https://psionofficial.com/Wireless/update.php",getIdGame());

            return conn.execute("u","-1","boardUpload","-1","-1",json.toString()).get();
        }catch(ExecutionException e){e.printStackTrace();}
        catch(InterruptedException e){e.printStackTrace();}
        return false;
    }
    public JSONObject buildBoardJSON(){
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
                        getPlayer1().setScore(howManyPawns(30,getPlayer1().getUserId()));
                        getPlayer2().setScore(howManyPawns(30,getPlayer2().getUserId()));
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
                        getPlayer1().setScore(howManyPawns(30,getPlayer1().getUserId()));
                        getPlayer2().setScore(howManyPawns(30,getPlayer2().getUserId()));
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

    public JSONObject updateRound(){
        phpConnect connTimeout = new phpConnect("https://psionofficial.com/Wireless/handler.php", getIdGame());
        try {
            if(connTimeout.execute("r", "GAME", Integer.toString(getPlayer1().getUserId()), "round", "-1").get())
                return connTimeout.getResJson();
        }catch(InterruptedException e ){e.printStackTrace();}
        catch(ExecutionException e ){e.printStackTrace();}

        return null;
    }


    public boolean finishGame(int playerId){
        phpConnect myConn = null;
        boolean result = false;
        try{
            myConn = new phpConnect("https://psionofficial.com/Wireless/handler.php",getIdGame());
            result = myConn.execute("u",Integer.toString(playerId),"endGame","-1","-1","-1").get();

        }catch(ExecutionException e){e.printStackTrace();}
        catch(InterruptedException e ){e.printStackTrace();}

        return result;

    }

    public dice getDice(){
        return diceRes;
    }



}
