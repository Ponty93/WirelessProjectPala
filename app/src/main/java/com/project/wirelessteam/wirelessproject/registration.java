package com.project.wirelessteam.wirelessproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



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
            name.setText("Questo campo non deve essere lasciato vuoto!");
            return false;
        }
    }

    class RegistrationSender extends AsyncTask<String,Void,Boolean> {
        String pass= ((TextView)findViewById(R.id.editText3)).getText().toString();
        String name= ((TextView)findViewById(R.id.editText2)).getText().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            phpConnect conn = new phpConnect("https://www.psionofficial.com/Wireless/register.php",0);
            conn.execute("c","USERS",name,pass);
            return conn.getResult();
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            //String  risultato=o.replaceAll("\\s+","");
            //Integer risultInt= Integer.parseInt(risultato);
            if(s == true){
                ((TextView) findViewById(R.id.textView6)).setText("REGISTRAZIONE EFFETTUATA!");
                goToLogin();
            }else{
                ((TextView) findViewById(R.id.textView6)).setText("NOME NON DISPONIBILE!");
            }
            System.out.println(s);
        }
    }

    private void goToLogin() {
        startActivity(new Intent(this,login.class));
    }
    public void sendRegistrationData(View view) {
       EditText editName = (EditText) findViewById(R.id.editText2);
       EditText editPass = (EditText) findViewById(R.id.editText3);

        if(checkInput(editName,editPass)){
            String name = editName.getText().toString();
            String pass = editPass.getText().toString();

            new RegistrationSender().execute();
        }
    }
}
