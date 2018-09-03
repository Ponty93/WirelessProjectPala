package com.project.wirelessteam.Views;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import API.phpConnect;
import Controller.lobbyController;
import Model.lobbyModel;

public class lobby extends AppCompatActivity {
    private Intent lobbyIntent;
    private JSONObject lobbyConn;
    private Chronometer timer;
    private AppCompatActivity currActivity =this;
    private Button startButton,stopButton;
    private lobbyController controller = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        lobbyIntent = getIntent();
        timer = (Chronometer) findViewById(R.id.timer);
        controller = new lobbyController(new lobbyModel(),this);

        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long minutes = ((SystemClock.elapsedRealtime() - timer.getBase())/1000) / 60;
                long seconds = ((SystemClock.elapsedRealtime() - timer.getBase())/1000) % 60;

                timer.setFormat(Long.toString(minutes)+":"+Long.toString(seconds));
                Log.d("MyTimer", "onChronometerTick: " + minutes + " : " + seconds);
                Log.d("MyCounter",""+seconds%10);

                if(minutes == 5L){
                    currActivity.finish();
                }

                if(seconds % 5 == 0){

                    lobbyConn = controller.lobbyCheck(lobbyIntent.getStringExtra("userName"),lobbyIntent.getStringExtra("idPlayer"));

                    try{
                    if(lobbyConn.getInt("Result") == 1) {
                        toBoard();
                    }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        });

        lobbyChecker();
    }

    private void toBoard(){

        JSONObject res =controller.toBoardConnect(lobbyIntent.getStringExtra("idPlayer"));

        Intent toBoard = new Intent(currActivity,BoardActivity.class);

        toBoard.putExtra("player1Id",Integer.parseInt(lobbyIntent.getStringExtra("idPlayer")));
        try {
            toBoard.putExtra("player2Id", lobbyConn.getInt("player2Id"));
            toBoard.putExtra("player2Name", lobbyConn.getString("player2Name"));
        }catch(JSONException e){
            e.printStackTrace();
        }
        toBoard.putExtra("json",res.toString());
        //put extraInt the id of the game object created
        startActivity(toBoard);
        finish();
    }

    public void lobbyChecker() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        stopButton.setEnabled(true);
    }


    public void back(View v){
        timer.stop();

        lobbyConn = controller.backAction(lobbyIntent.getStringExtra("userName"),lobbyIntent.getStringExtra("idPlayer"));
        try {
            if (lobbyConn.getInt("Result") == 1)
                toBoard();
            else
                super.finish();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


}