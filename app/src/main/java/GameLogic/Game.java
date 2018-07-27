package GameLogic;
import Actors.Player;
/**
 * Models a game in which two players play
 */


public class Game {
    private final Player player1;
    private final Player player2;
    private int counterPlayerOne;
    private int counterPlayerTwo;
    private int counter = 0;

    public Game(Player one, Player two) {
        player1 = one;
        player2 = two;
        counterPlayerOne = 6;
        counterPlayerTwo = 6;
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
    public int getCounterPlayerOne() {
        return counterPlayerOne;
    }
    public int getCounterPlayerTwo() {
        return counterPlayerTwo;
    }
    public int getCounter() {
        return counter;
    }

    /**
     * Set methods for the pawns counter of each player
     * @param val
     */
    public void setCounterPlayerOne(int val) {
        counterPlayerOne = val;
    }
    public void setCounterPlayerTwo(int val) {
        counterPlayerTwo = val;
    }

    /**
     * set the rounds counter
     * @param val
     */
    public void setCounter(int val) {
        counter = val;
    }

    /**
     * method to settle who starts first
     * Returns a boolean that rappresents the result
     * evens: true;
     * odds = false;
     * @return {boolean}
     */

    public boolean settleFirst() {
        int intResult = (int)Math.random()*3600+1;
        if((intResult % 2) == 0)
            return true;
        else
            return false;
    }

}
