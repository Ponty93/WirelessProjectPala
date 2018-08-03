package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import API.apiLibrary.src.main.java.com.goebl.david.*;
public class login extends AppCompatActivity {

String o;
    //private String Pass= ((TextView)findViewById(R.id.editText)).getText().toString();
    //private String Name= ((TextView)findViewById(R.id.editText2)).getText().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    class Controllo extends AsyncTask<String,Void,String> {
        String Pass= ((TextView)findViewById(R.id.editText4)).getText().toString();
        String Name= ((TextView)findViewById(R.id.editText)).getText().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Webb w = Webb.create();
             o = w.get("https://psionofficial.com/Wireless/login.php?Name=" + Name + "&Pass=" + Pass)
                    .ensureSuccess().asString().getBody();
            return o;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String  risultato=s.replaceAll("\\s+","");
            if(risultato.equals("1")){
                ((TextView) findViewById(R.id.textView2)).setText("LOGIN CORRETTO!");
            }else{
                ((TextView) findViewById(R.id.textView2)).setText("LOGIN ERRATO!");
            }
            System.out.println(s);
        }
    }
    public void sendLogin(View v) {

            new Controllo().execute();
    }

    public void goToRecord(View view) {
        Intent intent = new Intent(this,registration.class);
        startActivity(intent);
    }



}
