package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import java.util.Timer;
import java.util.TimerTask;

import API.phpConnect;
public class lobby extends AppCompatActivity {
    private Intent lobbyIntent;
    private phpConnect lobbyConn;
    private Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        lobbyIntent = getIntent();
        lobbyConn = new phpConnect(new String(), -1);
        timer = (Chronometer) findViewById(R.id.timer);


        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long minutes = ((SystemClock.elapsedRealtime() - timer.getBase())/1000) / 60;
                long seconds = ((SystemClock.elapsedRealtime() - timer.getBase())/1000) % 60;
                timer.setFormat(Long.toString(minutes)+":"+Long.toString(seconds));

                Log.d("MyTimer", "onChronometerTick: " + minutes + " : " + seconds);
            }
        });


        lobbyChecker();

    }

    public void lobbyChecker() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

    }

    public void stopButton(View view){
        timer.stop();
    }


}