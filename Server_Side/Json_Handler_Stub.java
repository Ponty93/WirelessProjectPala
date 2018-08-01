package com.example.acer.myapplication2.feature;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStreamReader;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private Button button2;

    class Controllo extends AsyncTask<String,Void,String> {
        String Pass= ((TextView)findViewById(R.id.editText)).getText().toString();
        String Name= ((TextView)findViewById(R.id.editText3)).getText().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb=null;
            BufferedReader reader=null;
            String serverResponse=null;
            try {

                URL url = new URL("https://psionofficial.com/Wireless/a.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.connect();
                int statusCode = connection.getResponseCode();
                //Log.e("statusCode", "" + statusCode);
                if (statusCode == 200) {
                    sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                }

                connection.disconnect();
                if (sb!=null)
                    serverResponse=sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return serverResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String risultato=s.toString();
            risultato=risultato.replaceAll("\\s+","");
            try {
                JSONObject jsonObj = new JSONObject(risultato);
                ((TextView)findViewById(R.id.textView)).setText(jsonObj.getString("test2String")) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }




            //Cose da fare quando completato collegamento
           System.out.println(s);
        }
    }

    public void ChangeTxt(View v) {

        new Controllo().execute();



    }


}
