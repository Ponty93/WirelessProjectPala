package GameLogic;
import Actors.Player;
/**
 * Models a game in which two players play
 */


public class Game {
    private final Player player1;
    private final Player player2;
    private int roundsCounter = 0;

    public Game() {
        boolean aux = settleFirst();
        player1 = new Player();
        player2 = new Player();

    }
    public Game(Player p1, Player p2) {

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


    /**
     * set the rounds counter
     * @param val
     */
    public void setCounter(int val) {
        roundsCounter = val;
    }

    /**
     * method to settle who starts first
     * Returns a boolean that rappresents the result
     * evens: true;
     * odds = false;
     * @return {boolean}
     */

    public static boolean settleFirst() {
        int intResult = (int)Math.random()*2+1;

        return intResult%2 ==0;

    }

    /**
     * Checks who plays in this round
     * @return {String}
     */
    public String whoPlay() {
        boolean aux = (roundsCounter%2 == 0);
        if(player1.getOrder() == aux)
            return player1.getUserName();
        else
            return player2.getUserName();
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

    /**
     * Checks how many points a player has
     * @param player
     * @return {int}
     */
    //public int howManyPoint(Player player)




}
