package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import API.phpConnect;

public class login extends AppCompatActivity {

 private boolean loginVar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void sendLogin(View view) {
        String Pass= ((TextView)findViewById(R.id.editText4)).getText().toString();
        String Name= ((TextView)findViewById(R.id.editText)).getText().toString();
        phpConnect conn = new phpConnect("https://psionofficial.com/Wireless/login.php",-1);
        try {
            conn.execute("r",Name,"-1",makeMD5(Name+Pass),"-2").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(conn.getResult() == true){
            ((TextView) findViewById(R.id.textView2)).setText("LOGIN CORRETTO!");
            loginVar = true;
            goToSetup();
        }
        else {
            ((TextView) findViewById(R.id.textView2)).setText("LOGIN ERRATO!");
            loginVar = false;
        }

    }

    private String makeMD5(String param) {
        StringBuffer sb = null;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((param).getBytes());
            byte[] digest = md.digest();
            sb = new StringBuffer();
            for(byte b : digest)
                sb.append(String.format("%02x", b & 0xff));

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return sb.toString();
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
