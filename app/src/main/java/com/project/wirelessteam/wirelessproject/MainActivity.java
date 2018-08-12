package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    private boolean checkIfAlreadyLogged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLogin();
    }

    //check if the user already authenticated or else head to the login page
    private void headToLogin() {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    private void headToSetup() {
        Intent intent = new Intent(this,setupPage.class);
        startActivity(intent);
    }

    //todo manage login credentials
    public void checkLogin() {
        if(checkIfAlreadyLogged == false)
            headToLogin();
        else
            headToSetup();
    }
}
