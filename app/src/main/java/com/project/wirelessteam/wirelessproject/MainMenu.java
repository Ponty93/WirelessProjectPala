package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
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
        Intent intent = new Intent(this, GameView.class);
        //passo id del player
        startActivity(intent);
    }
}
