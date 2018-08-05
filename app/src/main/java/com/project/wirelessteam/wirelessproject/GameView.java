package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import Actors.Player;
import GameLogic.Board;
public class GameView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        //faccio una post con id del player a scheduler php che mi ritorna una nuova board
        //con player1 e player2 scelti dallo scheduler con politica FIFO
        int id1=0, id2=0,gameId=0;
        String user1 = "Def1",user2="Def2";
        Intent intentToBoard = new Intent(this, BoardActivity.class);
        //passo un intent con id, userName player 1 e player 2 prelevati dalla query
        intentToBoard.putExtra("player1Id", id1);
        intentToBoard.putExtra("player1Name", user1);
        intentToBoard.putExtra("player2Id", id2);
        intentToBoard.putExtra("player2Name", user2);
        intentToBoard.putExtra("gameId",gameId);
        startActivity(intentToBoard);

    }
}
