package API;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import API.apiLibrary.src.main.java.com.goebl.david.*;
public final class phpConnect extends AsyncTask<String, Void,Boolean>{

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
        boolean resOperation = false;
        if(params[0] == "c")
            resOperation=create(params[0],params[1],params[2],params[3]);
        else if(params[0] == "r")
            resOperation=read(params[0],params[1],Integer.parseInt(params[2]),params[3],Integer.parseInt(params[4]));
        else if(params[0] == "u")
            resOperation=update(params[0],Integer.parseInt(params[1]),params[2],Integer.parseInt(params[3]),Integer.parseInt(params[4]),params[5]);
        else if(params[0] == "d")
            resOperation=delete(params[0],params[1],Integer.parseInt(params[2]));
        Log.d("VALORE RESOP" , String.valueOf(resOperation));
        return resOperation;
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
    private boolean read(String idOperation,String tableName,int playerId,String paramToRead, int valToRead) {
        try {

            //Log.d("entroInRead","Sto a inizio metodo");
            if(paramToRead.equals("pawnId")) {
                resJson = webb.post(url)
                        .param("gameId", gameId)
                        .param("tableName",tableName)
                        .param("pawnId", valToRead)
                        .param("playerId", playerId)
                        .param("op", idOperation)
                        .param("caseToQuery",1)
                        .ensureSuccess()
                        .asJsonObject()
                        .getBody();
            }
            else if(paramToRead.equals("position")) {
                resJson = webb.post(url)
                        .param("gameId", gameId)
                        .param("tableName",tableName)
                        .param("pos",valToRead )
                        .param("playerId", playerId)
                        .param("op", idOperation)
                        .param("caseToQuery",2)
                        .ensureSuccess()
                        .asJsonObject()
                        .getBody();
            }
            else if(paramToRead.equals("boardUpdate")){//todo remove useless
                resJson = webb.post(url)
                        .param("gameId", gameId)
                        .param("player1Id",playerId)
                        .param("tableName",tableName)
                        .param("player2Id",valToRead)
                        .param("op", idOperation)
                        .param("caseToQuery",3)
                        .ensureSuccess()
                        .asJsonObject()
                        .getBody();
            }
            else if(paramToRead.equals("round")){
                resJson = webb.post(url)
                        .param("gameId",gameId)
                        .param("playerId",playerId)
                        .param("round",paramToRead)
                        .param("op",idOperation)
                        .param("caseToQuery",4)
                        .ensureSuccess()
                        .asJsonObject()
                        .getBody();
            }
            else { //case that assume the read is not for the PAWN table but anyone else
                resJson = webb.post(url)
                        .param("tableName",tableName)
                        .param("op", idOperation)
                        .param("caseToQuery",5)
                        .param("attrToQuery",paramToRead)
                        .param("valToQuery",valToRead)
                        .ensureSuccess()
                        .asJsonObject()
                        .getBody();
            }
            if(Integer.parseInt(getParamFromJson("Result")) == 1){
                    //Log.d("READ" , "Sto a METTERE TRUE!");
                    //Log.d("valRead",getParamFromJson("Result"));
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
    private boolean create(String idOperation,String tableName,String val1,String val2) {
        try {
                resJson = webb.post(url)
                    .param("table",tableName)
                    .param("firstValToQuery",val1)
                    .param("secondValToQuery",val2)
                    .param("op", idOperation)
                    .ensureSuccess()
                    .asJsonObject()
                    .getBody();

            if(Integer.parseInt(getParamFromJson("Result")) == 1) {
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
    private boolean update(String idOperation,int playerId,String attrToQuery,int valToQuery,int posToUpdate,String jsonObj) {
        try {


                if(attrToQuery.equals("pawnId")) {
                resJson=
                        webb.post(url)
                                .param("gameId", gameId)
                                .param("op", idOperation)
                                .param("playerId", playerId)
                                .param("pawnId", valToQuery)
                                .param("posToUpdate", posToUpdate)
                                .ensureSuccess()
                                .asJsonObject()
                                .getBody();
            }
            else if(attrToQuery.equals("position")) {
                    resJson =
                            webb.post(url)
                                    .param("gameId", gameId)
                                    .param("op", idOperation)
                                    .param("playerId", playerId)
                                    .param("posToBeUpdated", valToQuery)
                                    .param("posToUpdate", posToUpdate)
                                    .ensureSuccess()
                                    .asJsonObject()
                                    .getBody();
                }
            else if(attrToQuery.equals("boardUpdate")){
                    resJson =
                            webb.post(url)
                                    .param("gameId", gameId)
                                    .param("op", idOperation)
                                    .param("boardUpdate",jsonObj)
                                    .ensureSuccess()
                                    .asJsonObject()
                                    .getBody();
                }
                else{
                    resJson =
                            webb.post(url)
                                    .param("gameId", gameId)
                                    .param("op", idOperation)
                                    .param("playerId", playerId)
                                    .param(attrToQuery, attrToQuery)
                                    .ensureSuccess()
                                    .asJsonObject()
                                    .getBody();
                }

            if(Integer.parseInt(getParamFromJson("Result")) == 1)
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
    private boolean delete(String idOperation,String tableName, int tableId) {
        try {
                resJson= webb.post(url)
                    .param("tableName",tableName)
                    .param("tableId",tableId)
                    .param("op", idOperation)
                    .ensureSuccess()
                    .asJsonObject()
                    .getBody();

            if(Integer.parseInt(getParamFromJson("Result")) == 1) {
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



    public String getUrl(){
        return url;
    }
    public void setUrl(String newUrl) {
        url = newUrl;
    }


}
