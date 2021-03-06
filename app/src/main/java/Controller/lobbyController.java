package Controller;

import com.project.wirelessteam.Views.lobby;

import org.json.JSONObject;

import Model.lobbyModel;

public class lobbyController {
    private lobbyModel refModel;
    private lobby lobbyView;

    public lobbyController(lobbyModel ref, lobby view){
        refModel = ref;
        lobbyView = view;
    }

    public JSONObject toBoardConnect(int playerId){
        return refModel.toBoardConnect(playerId);
    }

    public JSONObject lobbyCheck(String userName, int playerId){
        return refModel.lobbyCheck(userName,playerId);
    }

    public JSONObject backAction(String userName, int playerId){
        return refModel.backAction(userName,playerId);
    }
}
