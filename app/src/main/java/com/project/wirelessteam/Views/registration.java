package com.project.wirelessteam.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.util.concurrent.ExecutionException;

import API.phpConnect;

public class registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public boolean checkInput(EditText name, EditText pass) {
        if(!name.getText().toString().equals("") && !pass.getText().toString().equals(""))
            return true;
        else {
            name.setText("Questo campo non deve essere lasciato vuoto!"); //todo
            return false;
        }
    }

    private void goToLogin() {
        startActivity(new Intent(this,login.class));
    }
    public void sendRegistrationData(View view) {
       EditText editName = (EditText) findViewById(R.id.editText2);
       EditText editPass = (EditText) findViewById(R.id.editText3);

        if(checkInput(editName,editPass)){
            String pass= ((TextView)findViewById(R.id.editText3)).getText().toString();
            pass = pass.replaceAll("\\s","");
            String name= ((TextView)findViewById(R.id.editText2)).getText().toString();
            name = name.replaceAll("\\s","");
            phpConnect conn = new phpConnect("https://www.psionofficial.com/Wireless/register.php",-1);
            boolean aux = false;

            try {
                aux = conn.execute("c","USERS",name,pass,"1").get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(aux == true){
                ((TextView) findViewById(R.id.textView6)).setText("REGISTRAZIONE EFFETTUATA!");
                goToLogin();
            }else{
                ((TextView) findViewById(R.id.textView6)).setText("NOME NON DISPONIBILE!");
            }


        }
    }
}
