package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private boolean checkIfAlreadyLogged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void checkLogin() {
        if(checkIfAlreadyLogged == false)
            headToLogin();
        else
            headToSetup();
    }
}
