package com.project.wirelessteam.Views;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class setupPage extends AppCompatActivity {
    MediaPlayer mp;

    Intent myIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_page);
        myIntent = getIntent();
        TextView userName = (TextView) findViewById(R.id.displayUserName);
        userName.setText(myIntent.getStringExtra("userName"));
        mp = MediaPlayer.create(this, R.raw.menu);
        if(mp.isPlaying()){
            mp.stop();

        }else {
            mp.start();
            mp.setLooping(true);
        }
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

        Intent intentToLobby = new Intent(this,lobby.class);
        intentToLobby.putExtra("idPlayer",myIntent.getStringExtra("idPlayer"));
        intentToLobby.putExtra("userName",myIntent.getStringExtra("userName"));
        startActivity(intentToLobby);

    }
}
