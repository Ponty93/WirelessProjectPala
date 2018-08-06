package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import API.phpConnect;

public class login extends AppCompatActivity {

 private boolean loginVar = false;
    //private String Pass= ((TextView)findViewById(R.id.editText)).getText().toString();
    //private String Name= ((TextView)findViewById(R.id.editText2)).getText().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    class Controllo extends AsyncTask<String,Void,Boolean> {
        private String Pass= ((TextView)findViewById(R.id.editText4)).getText().toString();
        private String Name= ((TextView)findViewById(R.id.editText)).getText().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            phpConnect conn = new phpConnect("https://psionofficial.com/Wireless/login.php",-1);
            StringBuffer sb;
            try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((Name+Pass).getBytes());
            byte[] digest = md.digest();
            sb = new StringBuffer();
            for(byte b : digest)
                sb.append(String.format("%02x", b & 0xff));

            conn.execute("r","USERS","-1",sb.toString(),"-2");
            }catch(NoSuchAlgorithmException e){
                e.printStackTrace();
            }

            return conn.getResult();

        }

        @Override
        protected void onPostExecute(Boolean r) {
            if (r == true) {
                ((TextView) findViewById(R.id.textView2)).setText("LOGIN CORRETTO!");
                loginVar = true;
                goToSetup();

            } else {
                ((TextView) findViewById(R.id.textView2)).setText("LOGIN ERRATO!");
                loginVar = false;
            }
        }
    }
    public void sendLogin(View v) {

            new Controllo().execute();

    }

    private void goToSetup() {
        if(loginVar == true) {
            loginVar = false;
            startActivity(new Intent(this,setupPage.class));
        }

    }

    public void goToRecord(View view) {
        startActivity(new Intent(this,registration.class));
    }



}
