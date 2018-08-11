package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import API.phpConnect;
public class setupPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_page);
        Intent intent = getIntent();
    }

    /**
     * Switch view with the one that contains the best scores
     * @param view
     */
    public void goToHighlights(View view) {
        Intent intent = new Intent(this, Highlights.class);
        startActivity(intent);
    }

    /**
     * Switch view with the one to play a new game
     * @param view
     */
    public void goToNewGame(View view) {

        //retrieve local player data from login
        Intent intentFromLogin = getIntent();
        phpConnect conn = new phpConnect(new String(),-1); //todo
        String idPlayer1 =intentFromLogin.getStringExtra("idPlayer");
        String userNamePlayer1 = intentFromLogin.getStringExtra("userName");

        //query to retrieve new game instance data by passing local playerId
        conn.execute("r","GAME","-1","playerId",idPlayer1);

        //saving data from query to GAME table
        int gameId = Integer.parseInt(conn.getParamFromJson("gameId"));
        String idPlayer2 = conn.getParamFromJson("idPlayer2");

        //todo retrieve data about player2
        conn.setUrl(new String());
        conn.execute("r","PLAYER","-1","playerId",idPlayer2); //todo

        String userNamePlayer2 = conn.getParamFromJson("userName");

        //builds the intent to create the new local Board instance
        Intent intentToBoard = new Intent(this, BoardActivity.class);

        intentToBoard.putExtra("player1Id", idPlayer1);
        intentToBoard.putExtra("player1Name", userNamePlayer1);
        intentToBoard.putExtra("player2Id", idPlayer2);
        intentToBoard.putExtra("player2Name", userNamePlayer2);
        intentToBoard.putExtra("gameId",gameId);
        startActivity(intentToBoard);
    }
}
