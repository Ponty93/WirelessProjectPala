package com.project.wirelessteam.wirelessproject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import API.phpConnect;
public class lobby extends AppCompatActivity {
    private Intent lobbyIntent;
    private phpConnect lobbyConn;
    private Chronometer timer;
    private AppCompatActivity currActivity =this;
    private Button startButton,stopButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        lobbyIntent = getIntent();
        timer = (Chronometer) findViewById(R.id.timer);
        startButton = (Button)findViewById(R.id.start);
        stopButton = (Button) findViewById(R.id.stop);
        startButton.setEnabled(false);
        stopButton.setEnabled(false);

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
                boolean aux = false;

                if(seconds % 5 == 0){
                    try{
                        lobbyConn = new phpConnect("https://psionofficial.com/Wireless/lobby.php", -1);
                        aux = lobbyConn.execute("r","-1","-1",lobbyIntent.getStringExtra("userName"),lobbyIntent.getStringExtra("idPlayer")).get();
                    }
                    catch(InterruptedException e ){
                        e.printStackTrace();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    if(aux  == true)
                       // timer.stop();
                        toBoard();

                }

            }
        });

        lobbyChecker();
    }

    private void toBoard(){
        boolean aux = false;
        int gameId = 0;
        int roundPlayerId = -1;
        phpConnect myConn = null;
        try {
             myConn = new phpConnect("https://psionofficial.com/Wireless/handler.php", -1);
             aux = myConn.execute("r", "GAME","-1","gameId", lobbyIntent.getStringExtra("idPlayer")).get();
        }catch(InterruptedException e){e.printStackTrace();}
        catch(ExecutionException e){e.printStackTrace();}
        if(aux == true) {
            gameId = Integer.parseInt(myConn.getParamFromJson("gameId"));
            roundPlayerId = Integer.parseInt(myConn.getParamFromJson("filetto"));
        }

        Log.d("gameID","game id is "+gameId);

        String player2UserName = lobbyConn.getParamFromJson("player2Name");
        int player2Id = Integer.parseInt(lobbyConn.getParamFromJson("player2Id"));
        Intent toBoard = new Intent(currActivity,BoardActivity.class);
        toBoard.putExtra("player1Name",lobbyIntent.getStringExtra("userName"));
        toBoard.putExtra("player1Id",Integer.parseInt(lobbyIntent.getStringExtra("idPlayer")));
        toBoard.putExtra("player2Id",player2Id);
        toBoard.putExtra("player2Name",player2UserName);
        toBoard.putExtra("gameId",gameId);
        toBoard.putExtra("roundPlayerId",roundPlayerId);
        //put extraInt the id of the game object created
        startActivity(toBoard);
    }

    public void lobbyChecker() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        stopButton.setEnabled(true);
    }


    //todo remove in production (usefull to debug)
    public void startButton(View view){
        timer.start();
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    public void stopButton(View view){//todo
        //currActivity.finish();
        timer.stop();
        timer.setBase(SystemClock.elapsedRealtime());
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
    }

    public void back(View v){
        timer.stop();
        boolean reaux = false;
        try {
            lobbyConn = new phpConnect("https://psionofficial.com/Wireless/lobby.php", -1);
            reaux = lobbyConn.execute("r", "delete","-1",lobbyIntent.getStringExtra("userName"), lobbyIntent.getStringExtra("idPlayer")).get();
        }catch(InterruptedException e){e.printStackTrace();}
        catch(ExecutionException e){e.printStackTrace();}

        if(reaux == true)
            toBoard();
        else
            super.finish();

    }

}