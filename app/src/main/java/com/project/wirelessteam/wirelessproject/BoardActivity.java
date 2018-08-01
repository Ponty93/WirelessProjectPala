package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import Actors.Player;
import GameLogic.Board;
import GameLogic.Game;
public class BoardActivity extends AppCompatActivity {
    private Board currentBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent BuildBoard = getIntent();
        int id1 = BuildBoard.getIntExtra("player1Id",0);
        int id2 = BuildBoard.getIntExtra("player2Id",0);
        String user1 = BuildBoard.getStringExtra("player1Name");
        String user2 = BuildBoard.getStringExtra("player2Name");
        boolean roundOrder = Game.settleFirst();
        currentBoard = new Board(30,new Player(roundOrder,id1,user1),new Player(!roundOrder,id2,user2));
    }
}
