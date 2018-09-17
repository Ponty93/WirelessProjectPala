package com.project.wirelessteam.Views;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class setupPage extends AppCompatActivity {
    MediaPlayer mp;

    Intent myIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_page);
        myIntent = getIntent();
        mp = MediaPlayer.create(this, R.raw.menu);

    }
    @Override
    protected void onStart(){
        super.onStart();
        if(!mp.isPlaying()) {
            mp.start();
            mp.setLooping(true);
        }
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        mp.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        if(mp.isPlaying())
            mp.pause();
    }

    @Override
    protected  void onStop(){
        super.onStop();
        if(mp.isPlaying())
            mp.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mp.start();
    }

    /**
     * Switch view with the one that contains the best scores
     * @param view
     */
    public void goToHighlights(View view) {
        Intent intent = new Intent(this, Rules.class);
        startActivity(intent);
    }

    /**
     * Switch view with the one to play a new game
     * @param view
     */
    public void goToNewGame(View view) {
        Intent intentToLobby = new Intent(this,lobby.class);
        intentToLobby.putExtra("idPlayer",myIntent.getIntExtra("idPlayer",0));
        intentToLobby.putExtra("userName",myIntent.getStringExtra("userName"));
        startActivity(intentToLobby);

    }
}
