package com.project.wirelessteam.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import Controller.registrationController;
import Model.registrationModel;


public class registration extends AppCompatActivity {

    private registrationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        controller = new registrationController(this,new registrationModel());
    }

    public boolean checkInput(EditText name, EditText pass) {
        if(controller.checkInput(name,pass))
            return true;
        else {
            name.setText("Questo campo non deve essere lasciato vuoto!");
            return false;
        }
    }

    public void goToLogin() {
        startActivity(new Intent(this,login.class));
        finish();
    }

    public void sendRegistrationData(View view) {
       EditText editName = (EditText) findViewById(R.id.editText2);
       EditText editPass = (EditText) findViewById(R.id.editText3);

        if(checkInput(editName,editPass)){

            if(controller.goToLogin(editName,editPass)){
                ((TextView) findViewById(R.id.textView6)).setText("REGISTRAZIONE EFFETTUATA!");
                goToLogin();
            }
            else{
                ((TextView) findViewById(R.id.textView6)).setText("NOME NON DISPONIBILE!");
            }
        }
    }
}
