package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.UUID;

import API.apiLibrary.src.main.java.com.goebl.david.*;

public class registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }


    public void sendRegistrationData(View view) {
        Webb webb = Webb.create();
        Intent intent = new Intent(this, login.class);
        EditText editName = (EditText) findViewById(R.id.editText);
        EditText editPass = (EditText) findViewById(R.id.editText3);
        String name = editName.getText().toString();
        String pass = editPass.getText().toString();
        if(!name.equals("") && !pass.equals("")){
            UUID id = new UUID(16,48);
            try{
            webb.post(new String())
                    .param("userId",id)
                    .param("userName", name)
                    .param("password", pass)
                    .ensureSuccess()
                    .asVoid();}catch(Exception e ){e.printStackTrace();}
            startActivity(intent);
        }

        if(name.equals("") || pass.equals("")){
            editName.setText("Non deve essere lasciato vuoto");
        }



    }
}
