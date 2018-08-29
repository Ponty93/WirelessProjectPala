package com.project.wirelessteam.Views;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.project.wirelessteam.Views.david.Webb;
import com.project.wirelessteam.Views.david.Request;
import com.project.wirelessteam.Views.david.Response;
import com.project.wirelessteam.Views.david.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    /**
     * Switch view with the one that contains the best scores
     * @param view
     */
    public void goToHighlights(View view) {
        Intent intent = new Intent(this, Highlights.class);
        startActivity(intent);
    }

    public void goToNewGame(View view) {
        Intent intent = new Intent(this, GameView.class);
        startActivity(intent);
    }

    public void ESKERE(View view) {
        new ESKERECONNECTION().execute();
        //((TextView)findViewById(R.id.textView2)).setText("NICE");


    }
    /* Async task */
    private class ESKERECONNECTION extends AsyncTask<String, Void, String> {
        JSONObject o;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
          
             

            Webb w = Webb.create();
            o = w.get("https://psionofficial.com/Wireless/a.php").header("Authorization", "eskere")
                    .ensureSuccess().asJsonObject().getBody();


                //


            

            return null;
        }

        @Override
        protected void onPostExecute(String S) {
           super.onPostExecute(S);
            try {
                ((TextView)findViewById(R.id.textView2)).setText(o.getString("test2String"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    /**
     * Switch view with the one to play a new game
     * @param view
     */







}
