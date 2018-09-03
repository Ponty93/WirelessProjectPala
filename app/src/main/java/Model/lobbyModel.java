package Model;

import com.project.wirelessteam.Views.lobby;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import API.phpConnect;

public class lobbyModel {

    public JSONObject toBoardConnect(String playerId) {
        phpConnect myConn =null;
        boolean res = false;
        try {
            myConn = new phpConnect("https://psionofficial.com/Wireless/handler.php", -1);
            res = myConn.execute("r", "GAME","-1","gameId", playerId).get();
        }catch(InterruptedException e){e.printStackTrace();}
        catch(ExecutionException e){e.printStackTrace();}
        if(res == true)
            return myConn.getResJson();

        return null;
    }

    public JSONObject lobbyCheck(String userName, String playerId){
        phpConnect lobbyConn = null;
        boolean res = false;
        try{
            lobbyConn = new phpConnect("https://psionofficial.com/Wireless/lobby.php", -1);
            if(lobbyConn.execute("r","-1","-1",userName,playerId).get() == true)
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

    public JSONObject backAction(String userName, String playerId){
        phpConnect lobbyConn =null;
        try {
            lobbyConn = new phpConnect("https://psionofficial.com/Wireless/lobby.php", -1);
            if(lobbyConn.execute("r", "delete","-1",userName,playerId).get() == true)
                return lobbyConn.getResJson();
        }catch(InterruptedException e){e.printStackTrace();}
        catch(ExecutionException e){e.printStackTrace();}

        return null;
    }
}
