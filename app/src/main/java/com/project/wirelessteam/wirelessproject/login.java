package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import API.apiLibrary.src.main.java.com.goebl.david.*;
public class login extends AppCompatActivity {

 private String loginString = "";
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
             loginString = w.get("https://psionofficial.com/Wireless/login.php?Name=" + Name + "&Pass=" + Pass)
                    .ensureSuccess().asString().getBody();

            return loginString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String  risultato=s.replaceAll("\\s+","");
            if(risultato.equals("1")){
                ((TextView) findViewById(R.id.textView2)).setText("LOGIN CORRETTO!");
                loginString="";

            }else{
                ((TextView) findViewById(R.id.textView2)).setText("LOGIN ERRATO!");
            }
            System.out.println(s);
        }
    }
    public void sendLogin(View v) {

            new Controllo().execute();

            if(!loginString.equals("")){
                //((TextView) findViewById(R.id.textView2)).setText(loginString);
                loginString="";
                startActivity(new Intent(this,setupPage.class));
            }
    }

    public void goToRecord(View view) {
        startActivity(new Intent(this,registration.class));
    }



}
