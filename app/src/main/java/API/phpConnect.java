package API;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import API.apiLibrary.src.main.java.com.goebl.david.*;
public final class phpConnect extends AsyncTask<String, Void, Boolean>{

    private String url;
    private int gameId;
    private Webb webb = Webb.create();
    private JSONObject resJson = null;

    public phpConnect(String passedURL,int gameIdPassed) {
        url = passedURL;
        gameId = gameIdPassed;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    } //todo

    @Override
    protected Boolean doInBackground(String... params) {
        if(params[0] == "c")
            return create(params[0],params[1],params[2],params[3]);
        else if(params[0] == "r")
            return read(params[0],params[1],Integer.parseInt(params[2]),params[3],Integer.parseInt(params[4]));
        else if(params[0] == "u")
            return update(params[0],Integer.parseInt(params[1]),params[2],Integer.parseInt(params[3]),Integer.parseInt(params[4]));
        else if(params[0] == "d")
            return delete(params[0],params[1],Integer.parseInt(params[2]));

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
     * @param tableName : defines the table to query
     * @param playerId : userId of the player committing the operation
     * @param paramToRead : Choice to read by "pos" or by "pawnId"
     * @param valToRead : Val to use to read the query,it could be a pawn's position or a pawnId
     * @return {boolean}
     */
    private Boolean read(String idOperation,String tableName,int playerId,String paramToRead, int valToRead) {
        try {
            Response<JSONObject> response = null;

            if(paramToRead.equals("pawnId")) {
                response = webb.post(url)
                        .param("gameId", gameId)
                        .param("tableName",tableName)
                        .param("pawnId", valToRead)
                        .param("playerId", playerId)
                        .param("op", idOperation)
                        .param("caseToQuery",1)
                        .ensureSuccess()
                        .asJsonObject();
            }
            else if(paramToRead.equals("position")) {
                response = webb.post(url)
                        .param("gameId", gameId)
                        .param("tableName",tableName)
                        .param("pos",valToRead )
                        .param("playerId", playerId)
                        .param("op", idOperation)
                        .param("caseToQuery",2)
                        .ensureSuccess()
                        .asJsonObject();
            }
            else if(paramToRead.equals("boardUpdate")){
                response = webb.post(url)
                        .param("gameId", gameId)
                        .param("tableName",tableName)
                        .param("op", idOperation)
                        .param("caseToQuery",3)
                        .ensureSuccess()
                        .asJsonObject();
            }
            else { //case that assume the read is not for the PAWN table but anyone else
                response = webb.post(url)
                        .param("tableName",tableName)
                        .param("op", idOperation)
                        .param("caseToQuery",4)
                        .param("attrToQuery",paramToRead)
                        .param("valToQuery",valToRead)
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
     * @param tableName
     * @param val1
     * @param val2
     * @return {boolean}
     * tableName="GAME" : val1 = Player1Id val2 = Player2Id
     * tableName="Player" : val1: Username val2 = Password
     * tableName="Pawn" : val1 = GameId val2= PlayerId
     */
    private Boolean create(String idOperation,String tableName,String val1,String val2) {
        try {
            Response<Void> response= webb.post(url)
                    .param("table",tableName)
                    .param("firstValToQuery",val1)
                    .param("secondValToQuery",val2)
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
     * The method updates the posToBeUpdated position attribute with the new val posToUpdate
     * @param idOperation
     * @param playerId
     * @param attrToQuery: attribute to use in the making of the query
     * @param valToQuery : val to use in the making to the query, associated to attrToQuery
     * @param posToUpdate : new position's value
     * exclusive method for the PAWN table
     * @return {boolean}
     */
    private Boolean update(String idOperation,int playerId,String attrToQuery,int valToQuery,int posToUpdate) {
        try {

                Response<Void> response= null;
                if(attrToQuery.equals("pawnId")) {
                response =
                        webb.post(url)
                                .param("gameId", gameId)
                                .param("op", idOperation)
                                .param("playerId", playerId)
                                .param("pawnId", valToQuery)
                                .param("posToUpdate", posToUpdate)
                                .ensureSuccess()
                                .asVoid();
            }
            else if(attrToQuery.equals("position")) {
                    response =
                            webb.post(url)
                                    .param("gameId", gameId)
                                    .param("op", idOperation)
                                    .param("playerId", playerId)
                                    .param("posToBeUpdated", valToQuery)
                                    .param("posToUpdate", posToUpdate)
                                    .ensureSuccess()
                                    .asVoid();
                }

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
     * @param tableName
     * @param tableId
     * @return {boolean}
     */
    private Boolean delete(String idOperation,String tableName, int tableId) {
        try {
            Response<Void> response= webb.post(url)
                    .param("tableName",tableName)
                    .param("tableId",tableId)
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
