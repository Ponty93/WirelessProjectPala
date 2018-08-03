package API;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import API.apiLibrary.src.main.java.com.goebl.david.*;
public final class phpConnect extends AsyncTask<String, Void, Boolean>{

    private String url; //todo remove
    private UUID gameId;
    private Webb webb = Webb.create();
    private JSONObject resJson = null;

    public phpConnect(String passedURL,UUID gameIdPassed) {
        url = passedURL; //todo replace with the URL of the php page
        gameId = gameIdPassed;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    } //todo

    @Override
    protected Boolean doInBackground(String... params) {
        if(params[0] == "c")
            return create(params[0],Integer.parseInt(params[1]),Integer.parseInt(params[2]));
        else if(params[0] == "r")
            return read(params[0],Integer.parseInt(params[1]),params[2],Integer.parseInt(params[3]));
        else if(params[0] == "u")
            return update(params[0],Integer.parseInt(params[1]),params[2],Integer.parseInt(params[3]),params[4],params[5]);
        else if(params[0] == "d")
            return delete(params[0],Integer.parseInt(params[1]),Integer.parseInt(params[2]));

        return false;
    }

    /**
     * Operation allowed:
     * Create: 'c' builds a record in the table specified with the passed params
     * Read: 'r' read the record specified by the passed params
     * Update: 'u' update a record specified by the passed params
     * Delete: 'd' delete a record specified by the passed params
     */

    /**
     * The method reads a record from the table PAWN and returns his attribute and value as a JSON
     * @param idOperation : String to identify the operation to execute
     * @param playerId : userId of the player committing the operation
     * @param paramToRead : Choice to read by "pos" or by "pawnId"
     * @param valToRead : Val to use to read the query,it could be a pawn's position or a pawnId
     * @return {boolean}
     */
    private Boolean read(String idOperation,int playerId,String paramToRead, int valToRead) {
        try {
            Response<JSONObject> response = null;

            if(paramToRead.equals("pawnId")) {
                response = webb.post(url)
                        .param("gameId", gameId).param("pawnId", valToRead).param("playerId", playerId).param("op", idOperation)
                        .ensureSuccess()
                        .asJsonObject();
            }
            else if(paramToRead.equals("pos")) {
                response = webb.post(url)
                        .param("gameId", gameId).param("pos",valToRead ).param("playerId", playerId).param("op", idOperation)
                        .ensureSuccess()
                        .asJsonObject();
            }
            if(response.getStatusCode() == 200) {
                resJson = response.getBody();
                return true;
            }
        }
        catch(Exception e ){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * The method builds a new record in the PAWN table
     * @param idOperation
     * @param playerId
     * @param pawnId
     * @return {boolean}
     */
    private Boolean create(String idOperation,int playerId,int pawnId) {
        try {
            Response<Void> response= webb.post(url)
                    .param("gameId", gameId)
                    .param("pawnId",pawnId)
                    .param("playerId",playerId)
                    .param("op", idOperation)
                    .ensureSuccess()
                    .asVoid();
            if(response.getStatusCode() == 200) {
                return true;
            }
        }
        catch(Exception e ){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * The method updates the paramToUpdate attribute with the new val value
     * @param idOperation
     * @param playerId
     * @param attrToQuery : Choice to query by position or by pawnId
     * @param valToQuery : pawnId or position value
     * @param paramToUpdate : attribute of the PAWN table to be updated
     * @param val : new value to be updated to the paramToUpdate's attribute
     * @return {boolean}
     */
    private Boolean update(String idOperation,int playerId,String attrToQuery,int valToQuery,String paramToUpdate, String val) {
        try {
            Response<Void> response = webb.post(url)
                        .param("gameId", gameId)
                    .param(attrToQuery, valToQuery)
                    .param("playerId", playerId)
                    .param("op", idOperation)
                    .param("paramUpdate", paramToUpdate)
                    .param("valToUpdate", val)
                    .ensureSuccess()
                    .asVoid();


            if(response.getStatusCode() == 200)
                return true;

        }
        catch(Exception e ){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * The method deletes a record in the PAWN table
     * @param idOperation
     * @param playerId
     * @param pawnId
     * @return {boolean}
     */
    private Boolean delete(String idOperation,int playerId,int pawnId) {
        try {
            Response<Void> response= webb.post(url)
                    .param("gameId", gameId).param("pawnId",pawnId)
                    .param("playerId",playerId)
                    .param("op", idOperation)
                    .ensureSuccess()
                    .asVoid();
            if(response.getStatusCode() == 200) {
                return true;
            }
        }
        catch(Exception e ){
            e.printStackTrace();
        }
        return false;
    }


    public JSONObject getResJson() {
        return resJson;
    }

    /**
     * The method returns the value of the attribute attr in the json
     * @param attr
     * @return {boolean}
     */
    public String getParamFromJson(String attr) {
        try{
            String aux = resJson.getString(attr);
            if(aux != null) {
                return aux;
            }

        }
        catch(JSONException e ){
            e.printStackTrace();
        }
        return null;
    }


}
