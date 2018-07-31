import android.os.AsyncTask;

import org.json.JSONObject;
import apiLibrary.src.main.java.com.goebl.david.*;
public final class phpConnect extends AsyncTask<String,Void,JSONObject> {

    private String url;
    private int gameId;
    private int pawnId;
    private int playerId;
    private char charOperation;
    private Webb webb = Webb.create();


    public phpConnect(String passedURL,int gameIdPassed, int pawnIdPassed,int playerIdPassed, char charOperationPassed) {
        url = passedURL;
        gameId = gameIdPassed;
        pawnId = pawnIdPassed;
        playerId = playerIdPassed;
        charOperation = charOperationPassed;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject res = null;
        try {
             res = webb.post(url)
                .param("gameId", gameId).param("pawnId",pawnId).param("playerId",playerId).param("op", charOperation)
                .ensureSuccess()
                .asJsonObject()
                .getBody();

        }
        catch(Exception e ){
            e.printStackTrace();
        }finally {
            webb.delete(url).asVoid();
        }
        return res;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        System.out.print(s.toString());
        //todo

    }


}
