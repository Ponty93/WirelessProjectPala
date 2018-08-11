package com.project.wirelessteam.wirelessproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

import Actors.Pawn;
import Actors.Player;
import GameLogic.Board;
import GameLogic.Game;
import API.phpConnect;
public class BoardActivity extends AppCompatActivity {
    private Board currentBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent BuildBoard = getIntent();
        int id1 = BuildBoard.getIntExtra("player1Id", 0);
        int id2 = BuildBoard.getIntExtra("player2Id", 0);
        int gameId = BuildBoard.getIntExtra("gameId", 0);
        String user1 = BuildBoard.getStringExtra("player1Name");
        String user2 = BuildBoard.getStringExtra("player2Name");
        boolean roundOrder = settleFirst();
        currentBoard = new Board(30, new Player(roundOrder, id1, user1), new Player(!roundOrder, id2, user2), gameId);
        setPawnView(this,currentBoard.getPlayer1().getPawns());
        setPawnView(this,currentBoard.getPlayer2().getPawns());
    }

    private void setPawnView(Context context, HashMap<Integer, Pawn> map){
        for(int i=0;i<map.size();i++)
            map.get(i).setImage(context);
    }


    /**
     * method to settle who starts first
     * Returns a boolean that rappresents the result
     * evens: true;
     * odds = false;
     * @return {boolean}
     */

    public boolean settleFirst() {
        int intResult = (int)Math.random()*6+1;
        //todo game.php roundOrder
        phpConnect tmpConn = new phpConnect("https://www.psionofficial.com/Wireless/handler.php",-1);
        tmpConn.execute("r","GAME","-1","diceValue",Integer.toString(intResult));
        return Boolean.parseBoolean(tmpConn.getParamFromJson("roundOrder"));
    }

    //todo onTOuch method
}
