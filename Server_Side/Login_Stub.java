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

                URL url = new URL("https://psionofficial.com/Wireless/login.php?Name=" + Name + "&Pass=" + Pass);
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
            String vero="1";
            String risultato=s.toString();
            risultato=risultato.replaceAll("\\s+","");
            if(risultato.equals(vero)){
            ((TextView)findViewById(R.id.textView)).setText("LOGIN CORRETTO!");}else{
                ((TextView)findViewById(R.id.textView)).setText("LOGIN FALLITO!") ;
            }
            //Cose da fare quando completato collegamento
           System.out.println(s);
        }
    }

    public void ChangeTxt(View v) {

        new Controllo().execute();



    }
