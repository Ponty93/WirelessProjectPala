package Model;

import android.util.Log;

import com.project.wirelessteam.Views.lobby;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import API.phpConnect;

public class lobbyModel {

    public JSONObject toBoardConnect(int playerId) {
        phpConnect myConn;
        try {
            myConn = new phpConnect("https://psionofficial.com/Wireless/handler.php", -1);
            if(myConn.execute("r", "GAME","-1","gameId", Integer.toString(playerId)).get() == true) {
               // Log.d("JSON","is"+myConn.getResJson());
                return myConn.getResJson();
            }
        }catch(InterruptedException e){e.printStackTrace();}
        catch(ExecutionException e){e.printStackTrace();}

        return null;
    }

    public JSONObject lobbyCheck(String userName, int playerId){
        phpConnect lobbyConn = null;
        boolean res = false;
        try{
            lobbyConn = new phpConnect("https://psionofficial.com/Wireless/lobby.php", -1);
            if(lobbyConn.execute("r","-1","-1",userName,Integer.toString(playerId)).get() == true)
                return lobbyConn.getResJson();
        }
        catch(InterruptedException e ){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public JSONObject backAction(String userName, int playerId){
        phpConnect lobbyConn =null;
        try {
            lobbyConn = new phpConnect("https://psionofficial.com/Wireless/lobby.php", -1);
            if(lobbyConn.execute("r", "delete","-1",userName,Integer.toString(playerId)).get() == true)
                return lobbyConn.getResJson();
        }catch(InterruptedException e){e.printStackTrace();}
        catch(ExecutionException e){e.printStackTrace();}

        return null;
    }
}
