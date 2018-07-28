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
    private HashMap<Integer,Integer> mapPawnsOnBoard = new HashMap<>();
    private int[] diceBuffer = new int[2];
    /**
     * builds the map putting the id of the pawns for each player
     * id[0..5] belongs to player 1
     * id[6..11] belongs to player 2
     */
    private void buildMap() {
        for(int i=0; i<12;i++)
            mapPawnsOnBoard.put(i,-1);
    }

    /**
     * create a Board instance to begin the game
     * @param cells define the number of cells the board has
     */
    public Board(int cells) {
        super();
        numberOfCell = cells;
        buildMap();
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
    public void planToMove(int v1, int id1, int v2, int id2) {
            movePawn(v1,findPlayerByUserName(whoPlay()).findPawnById(id1));
            if(id2 != -1)
                movePawn(v1,findPlayerByUserName(whoPlay()).findPawnById(id1));
    }

    /**
     * Move a pawn according to the values passed as param
     * The method also check if the pawn is at the end line or is over that
     * @param val
     * @param pawn
     */
    public void movePawn(int val, Pawn pawn) {
        if(whoPlay() == getPlayer1().getUserName()) {
            if(mapPawnsOnBoard.get(0 + pawn.getId()) + val == numberOfCell-1)
                mapPawnsOnBoard.put(0 + pawn.getId(),numberOfCell);
            else if(mapPawnsOnBoard.get(0 + pawn.getId()) + val >=numberOfCell)
                return;
            else {
                if (mapPawnsOnBoard.get(0 + pawn.getId()) != -1)
                    mapPawnsOnBoard.put(0 + pawn.getId(), mapPawnsOnBoard.get(0 + pawn.getId()) + val);
                else
                    mapPawnsOnBoard.put(0 + pawn.getId(), 0 + val);
            }
        }
        else {
            if(mapPawnsOnBoard.get(5 + pawn.getId()) + val == numberOfCell-1)
                mapPawnsOnBoard.put(5 + pawn.getId(),numberOfCell);
            else if(mapPawnsOnBoard.get(5 + pawn.getId()) + val >=numberOfCell)
                return;
            else {
                if (mapPawnsOnBoard.get(5 + pawn.getId()) != -1)
                    mapPawnsOnBoard.put(5 + pawn.getId(), mapPawnsOnBoard.get(5 + pawn.getId()) + val);
                else
                    mapPawnsOnBoard.put(5 + pawn.getId(), 0 + val);
            }
        }

    }





}
