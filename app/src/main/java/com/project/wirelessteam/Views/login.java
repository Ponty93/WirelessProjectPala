package com.project.wirelessteam.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;




import API.phpConnect;
import Controller.loginController;
import Model.loginModel;

public class login extends AppCompatActivity {

    private loginController controller;
    private JSONObject conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        controller = new loginController(this,new loginModel());
    }

    public void sendLogin(View view) {

        conn = controller.sendLogin((EditText) findViewById(R.id.editText),(EditText) findViewById(R.id.editText4));

        try {
            if (conn.getInt("Result") == 1) {
                ((TextView) findViewById(R.id.textView2)).setText("LOGIN CORRETTO!");
                goToSetup();
            } else {
                ((TextView) findViewById(R.id.textView2)).setText("LOGIN ERRATO!");

            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    private void goToSetup() {
        try{
            if(conn.getInt("Result") == 1) {

                Intent intent = new Intent(this, setupPage.class);

                intent.putExtra("idPlayer", conn.getInt("playerId"));
                intent.putExtra("userName", conn.getString("userName"));
                startActivity(intent);
            }
        }catch(JSONException e){
                e.printStackTrace();
            }
    }

    public void goToRecord(View view) {
        startActivity(new Intent(this,registration.class));
    }



}
