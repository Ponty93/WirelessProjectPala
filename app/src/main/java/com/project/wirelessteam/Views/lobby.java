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

    private lobbyController controller = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        lobbyIntent = getIntent();
        timer = (Chronometer) findViewById(R.id.timer);
        timer.setFormat("%s");
        controller = new lobbyController(new lobbyModel(),this);

        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long minutes = ((SystemClock.elapsedRealtime() - timer.getBase())/1000) / 60;
                long seconds = ((SystemClock.elapsedRealtime() - timer.getBase())/1000) % 60;


                Log.d("MyTimer", "onChronometerTick: " + minutes + " : " + seconds);
                Log.d("MyCounter",""+seconds%10);

                if(minutes == 5L){
                    currActivity.finish();
                }

                if(seconds % 5 == 0) {

                    lobbyConn = controller.lobbyCheck(lobbyIntent.getStringExtra("userName"), lobbyIntent.getIntExtra("idPlayer",0));
                    if (lobbyConn != null) {
                        try {
                            if (lobbyConn.getInt("Result") == 1)
                                toBoard();


                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }
    @Override
    protected void onStop(){
        super.onStop();
        timer.stop();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //cancella coda
    }
    private void toBoard(){

        JSONObject res =controller.toBoardConnect(lobbyIntent.getIntExtra("idPlayer",0));

        Intent toBoard = new Intent(currActivity,BoardActivity.class);

        toBoard.putExtra("player1Id",lobbyIntent.getIntExtra("idPlayer",0));
        toBoard.putExtra("player1Name",lobbyIntent.getStringExtra("userName"));

        try {
            toBoard.putExtra("player2Id", lobbyConn.getInt("player2Id"));
            toBoard.putExtra("player2Name", lobbyConn.getString("player2Name"));
        }catch(JSONException e){
            e.printStackTrace();
        }
        if(res != null) {
            //Log.d("JSON","is"+res.toString());
            toBoard.putExtra("json", res.toString());
            startActivity(toBoard);
            finish();
        }
        else
            super.finish();

    }


    public void back(View v){
        timer.stop();

        lobbyConn = controller.backAction(lobbyIntent.getStringExtra("userName"),lobbyIntent.getIntExtra("idPlayer",0));
        try {
            if(lobbyConn != null) {
                if (lobbyConn.getInt("Result") == 1)
                    toBoard();
            }
            else
                super.finish();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


}