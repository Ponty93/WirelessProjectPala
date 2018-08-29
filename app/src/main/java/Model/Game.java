package Model;
import API.phpConnect;
import Actors.Player;

/**
 * Models a game in which two players play
 */


public class Game {
    private final int idGame;
    private final Player player1;
    private final Player player2;
    private int roundsCounter = 0;
    //todo remove
    private phpConnect connectionHandler = new phpConnect("https://www.psionofficial.com/Wireless/handler.php",getIdGame());


    public Game(Player p1, Player p2,int gameId) {
        idGame = gameId;
        player1 = p1;
        player2 = p2;
    }

    /**
     * Get methods for the data fields
     * @return {Player} ,{int}
     */
    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }
    public int getRoundsCounter() {
        return roundsCounter;
    }

    public int getIdGame() {
        return idGame;
    }

    /**
     * set the rounds counter
     * @param val
     */
    public void setCounter(int val) {
        roundsCounter = val;
    }


    /**
     * Searches for a player by passing an id
     * @param id
     * @return {Player}
     */
    public Player findPlayerById(int id) {
        if(id == player1.getUserId())
            return player1;
        else
            return player2;
    }

    public Player findPlayerByRemoving(int id) {
        if(id == player1.getUserId())
            return player2;
        else
            return player1;
    }

    public phpConnect getConnection() {
        return connectionHandler;
    }

    public boolean playerOrder(int id){
        if(id == getPlayer1().getUserId())
            return true;
        else
            return false;
    }



}
