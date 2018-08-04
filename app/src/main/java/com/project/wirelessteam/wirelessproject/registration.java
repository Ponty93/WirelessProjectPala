package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

import API.apiLibrary.src.main.java.com.goebl.david.*;

public class registration extends AppCompatActivity {
String o;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public boolean checkInput(EditText name, EditText pass) {
        if(!name.getText().toString().equals("") && !pass.getText().toString().equals(""))
            return true;
        else {
            name.setText("Questo campo non deve essere lasciato vuoto!");
            return false;
        }
    }

    class RegistrationSender extends AsyncTask<String,Void,String> {
        String pass= ((TextView)findViewById(R.id.editText3)).getText().toString();
        String name= ((TextView)findViewById(R.id.editText2)).getText().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Webb webb = Webb.create();
            o = webb.post("https://www.psionofficial.com/Wireless/register.php")
                    .param("userName", name)
                    .param("password", pass)
                    .ensureSuccess()
                    .asString().getBody();
            return o;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String  risultato=o.replaceAll("\\s+","");
            Integer risultInt= Integer.parseInt(risultato);
            if(risultInt!=0){
                ((TextView) findViewById(R.id.textView6)).setText("REGISTRAZIONE EFFETTUATA!");
                //here we should save the ID the php page sent us and setup the local login memory.
            }else{
                ((TextView) findViewById(R.id.textView6)).setText("NOME NON DISPONIBILE!");
            }
            System.out.println(s);
        }
    }
    public void sendRegistrationData(View view) {
       // Intent intent = new Intent(this, login.class);
       EditText editName = (EditText) findViewById(R.id.editText2);
       EditText editPass = (EditText) findViewById(R.id.editText3);

        //if(checkInput(editName,editPass)){
            String name = editName.getText().toString();
            String pass = editPass.getText().toString();
           // UUID id = new UUID(16,48); unuseful since the db decides the id.
            new RegistrationSender().execute();

            // startActivity(intent);
        //}



    }
}
