package com.project.wirelessteam.Views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import Actors.Pawn;
import Actors.Player;
import Model.Board;
import API.phpConnect;
import Utils.onTouchCustomMethod;
import Utils.onDragCustomMethod;


public class BoardActivity extends AppCompatActivity {
    private Board currentBoard = null;
    private Context context = null;
    private RelativeLayout refLayout=null;
    private final BoardActivity boardView = this;
    private Timer internalTimer;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent buildBoard = getIntent();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //initialize the boards view feature
        init(buildBoard);
        //todo send a req to reload the data if any

    }

    public Timer getTimerInstance(){
        if(internalTimer == null) {
            internalTimer = new Timer();
        }

        return internalTimer;
    }

    protected class roundTimeout extends TimerTask {
        private int counter = 0;
        private BoardActivity refBoard;

        roundTimeout(BoardActivity board) {
            refBoard = board;
        }

        @Override
        public void run() {

            counter++;

            if(counter == 24){
                currentBoard.surrender();
                endGame(refBoard);
            }

            Log.d("COUNTER END ROUND","COUNTER IS:"+counter);
            //Log.d("roundTimeout", "round timeout occurred");
            boolean connRes = false;
            phpConnect connTimeout = new phpConnect("https://psionofficial.com/Wireless/handler.php", currentBoard.getIdGame());
            connRes = getCurrentBoard().updateRound(connTimeout);
            //Log.d("JSON", "Json res" + connTimeout.getResJson());
            if (connRes == true) {
                if (connTimeout.getParamFromJson("winner").equals("none") == false) {
                    if (Integer.parseInt(connTimeout.getParamFromJson("winner")) == currentBoard.getPlayer1().getUserId()) {
                        endGame(refBoard);
                        //todo display hai vinto
                    }
                }
            }
        }
    }
    /**
     * class to model a connectionTimeout task
     */
    //class created only when its not my round
    protected class connectionTimeout extends TimerTask {
        private BoardActivity refBoard;

        public connectionTimeout(BoardActivity board){
            refBoard = board;
        }

        @Override
        public void run() {

            Log.d("ConnectionTimeout","Connection timeout occurred ");
            boolean connRes = false;
            final phpConnect connTimeout = new phpConnect("https://psionofficial.com/Wireless/handler.php", currentBoard.getIdGame());
            connRes = getCurrentBoard().updateRound(connTimeout);
            //Log.d("JSON","Json res"+connTimeout.getResJson());
            if(connRes == true){
                Log.d("RECEIVER","CONNRES IS TRUE");
                if(connTimeout.getParamFromJson("winner").equals("none") == false) {
                    Log.d("RECEIVER","WINNER IS ID");
                   if (Integer.parseInt(connTimeout.getParamFromJson("winner")) == refBoard.getCurrentBoard().getPlayer1().getUserId()){
                       endGame(refBoard);
                        //todo display hai vinto
                    }
                   else {
                       //todo hai perso : avversario è arrivato con i 6 pawn al finish
                   }
                }
                else {
                    //Log.d("ROUND","ITS FINALLY MY ROUND");

                    refBoard.getTimerInstance().cancel();
                    refBoard.internalTimer = null; //todo
                    //it permits to call the UI thread to exec the Views operation
                    //timer runs on a different thread, so it does not have access to modify views
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateBoard(connTimeout.getResJson());
                            roundOrganize(true);
                        }
                    });

                }

            }
        }
    }

    /**
     * regolate the players round
     * @param round
     */
    private void roundOrganize(boolean round) {
        //true : my round
        //false : foe round
        if(round == false) {
            findViewById(R.id.red1).setEnabled(round);
            findViewById(R.id.red2).setEnabled(round);
            findViewById(R.id.red3).setEnabled(round);
            findViewById(R.id.red4).setEnabled(round);
            findViewById(R.id.red5).setEnabled(round);
            findViewById(R.id.red6).setEnabled(round);
        }

        findViewById(R.id.roll).setEnabled(round);
        //display a message "waiting for foe to finish his turn.."

        //increments the players rounds
        // sets to 0 the number of move in this round
        // sets the foe score
        if(round == true) {
            currentBoard.setDiceResToNull();
            currentBoard.setNumberOfMove(0);
            getCurrentBoard().setCounter(getCurrentBoard().getRoundsCounter() + 1);
            ((TextView)findViewById(R.id.roundNumber)).setText(Integer.toString(getCurrentBoard().getRoundsCounter()));
            ((TextView)findViewById(R.id.scoreAvv)).setText(Integer.toString(getCurrentBoard().getPlayer2().getScore()));
        }


        //timer to schedule action
        if(round == true) {//if its my turn, at 2m calls endTurn
            Log.d("internalTimer","starts my round timer");
            getTimerInstance().schedule(new roundTimeout(boardView),0,5000);
        }
        else if(round == false){//its not my turn, every 10s i ask the server if its my turn now
            Log.d("internalTimer","starts NOT my round timer");
            getTimerInstance().schedule(new connectionTimeout(boardView),0,5000);
        }


        //enables or disables the endTurn button
        if(round == true){
            findViewById(R.id.endTurn).setEnabled(true);
        }
        else {
            findViewById(R.id.endTurn).setEnabled(false);
        }
    }

    private void init(Intent buildBoard) {

        if(context == null)
            context = getApplicationContext();

        //init the board and the retrieve the data for player 2
        TextView idPl = (TextView) findViewById(R.id.idPL) ;
        idPl.setText(Integer.toString(buildBoard.getIntExtra("player1Id",0)));
        TextView userPl =(TextView)findViewById(R.id.userPL);
        userPl.setText(buildBoard.getStringExtra("player1Name"));
        TextView idAvv = (TextView) findViewById(R.id.idAvv);
        TextView userAvv = (TextView) findViewById(R.id.userAvv);
        idAvv.setText(Integer.toString(buildBoard.getIntExtra("player2Id",0)));
        userAvv.setText(buildBoard.getStringExtra("player2Name"));
        int id1 = buildBoard.getIntExtra("player1Id", 0);
        int id2 = buildBoard.getIntExtra("player2Id", 0);

        //Log.d("gameID","gameId is" +gameId);
        String user1 = buildBoard.getStringExtra("player1Name");
        String user2 = buildBoard.getStringExtra("player2Name");


        //Log.d("Json ",buildBoard.getStringExtra("json"));

        JSONObject jsonP1 = null,jsonP2=null,json=null;
        int gameId=0,roundId=0;
        try {
            json= new JSONObject(buildBoard.getStringExtra("json"));
            Log.d("JSON INTERO","is"+json);

            //update board
            updateBoard(json);

           //Log.d("JSONP1","is"+jsonP1);
            //Log.d("JSONP2","is"+jsonP2);
            gameId =json.getInt("gameId");
            roundId = json.getInt("filetto");
        }catch(JSONException e){e.printStackTrace();}

        if(currentBoard == null) {
            if(jsonP1!=null && jsonP2!=null) {
                currentBoard = new Board(30, new Player(id1, user1,jsonP1), new Player(id2, user2,jsonP2), gameId);
                setPawnView(context, currentBoard.getPlayer1().getPawns(), "player1");
                setPawnView(context, currentBoard.getPlayer2().getPawns(), "player2");
            }
        }

        if(refLayout == null){
            refLayout = (RelativeLayout) findViewById(R.id.rel1);
        }



        //Define the player1 pawns drag and drop
        ImageView red1 = findViewById(R.id.red1);
        red1.setOnTouchListener(new onTouchCustomMethod(context,refLayout,currentBoard));
        red1.setTag("red1");

        ImageView red2 = findViewById(R.id.red2);
        red2.setOnTouchListener(new onTouchCustomMethod(context,refLayout,currentBoard));
        red2.setTag("red2");

        ImageView red3 = findViewById(R.id.red3);
        red3.setOnTouchListener(new onTouchCustomMethod(context,refLayout,currentBoard));
        red3.setTag("red3");

        ImageView red4 = findViewById(R.id.red4);
        red4.setOnTouchListener(new onTouchCustomMethod(context,refLayout,currentBoard));
        red4.setTag("red4");

        ImageView red5 = findViewById(R.id.red5);
        red5.setOnTouchListener(new onTouchCustomMethod(context,refLayout,currentBoard));
        red5.setTag("red5");

        ImageView red6 = findViewById(R.id.red6);
        red6.setOnTouchListener(new onTouchCustomMethod(context,refLayout,currentBoard));
        red6.setTag("red6");


        //abilitate to move the pawns

        roundOrganize(currentBoard.playerOrder(roundId));

        //sets the cell able to receive the pawns
        findViewById(R.id.cell1L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell1L).setTag("0");
        findViewById(R.id.cell2L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell2L).setTag("1");
        findViewById(R.id.cell3L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell3L).setTag("2");
        findViewById(R.id.cell4L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell4L).setTag("3");
        findViewById(R.id.cell5L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell5L).setTag("4");
        findViewById(R.id.cell6L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell6L).setTag("5");
        findViewById(R.id.cell7L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell7L).setTag("6");
        findViewById(R.id.cell8L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell8L).setTag("7");
        findViewById(R.id.cell9L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell9L).setTag("8");
        findViewById(R.id.cell10L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell10L).setTag("9");
        findViewById(R.id.cell11L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell11L).setTag("10");
        findViewById(R.id.cell12L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell12L).setTag("11");
        findViewById(R.id.cell13L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell13L).setTag("12");
        findViewById(R.id.cell14L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell14L).setTag("13");
        findViewById(R.id.cell15L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell15L).setTag("14");
        findViewById(R.id.cell16L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell16L).setTag("15");
        findViewById(R.id.cell17L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell17L).setTag("16");
        findViewById(R.id.cell18L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell18L).setTag("17");
        findViewById(R.id.cell19L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell19L).setTag("18");
        findViewById(R.id.cell20L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell20L).setTag("19");
        findViewById(R.id.cell21L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell21L).setTag("20");
        findViewById(R.id.cell22L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell22L).setTag("21");
        findViewById(R.id.cell23L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell23L).setTag("22");
        findViewById(R.id.cell24L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell24L).setTag("23");
        findViewById(R.id.cell25L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell25L).setTag("24");
        findViewById(R.id.cell26L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell26L).setTag("25");
        findViewById(R.id.cell27L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell27L).setTag("26");
        findViewById(R.id.cell28L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell28L).setTag("27");
        findViewById(R.id.cell29L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell29L).setTag("28");
        findViewById(R.id.cell30L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell30L).setTag("29");
        findViewById(R.id.cell31L).setOnDragListener(new onDragCustomMethod(boardView,currentBoard));
        findViewById(R.id.cell31L).setTag("30");
    }



    //links the pawns object to the pawns views
    private void setPawnView(Context context,HashMap<Integer, Pawn> map, String player){
        if(player.equals("player1")) {
            map.get(1).setPawnView((ImageView)findViewById(R.id.red1));
            map.get(2).setPawnView((ImageView)findViewById(R.id.red2));
            map.get(3).setPawnView((ImageView)findViewById(R.id.red3));
            map.get(4).setPawnView((ImageView)findViewById(R.id.red4));
            map.get(5).setPawnView((ImageView)findViewById(R.id.red5));
            map.get(6).setPawnView((ImageView)findViewById(R.id.red6));


        }
        else {
            map.get(1).setPawnView((ImageView)findViewById(R.id.black1));
            map.get(2).setPawnView((ImageView)findViewById(R.id.black2));
            map.get(3).setPawnView((ImageView)findViewById(R.id.black3));
            map.get(4).setPawnView((ImageView)findViewById(R.id.black4));
            map.get(5).setPawnView((ImageView)findViewById(R.id.black5));
            map.get(6).setPawnView((ImageView)findViewById(R.id.black6));
        }
    }



    //todo move in a controller class
    public boolean CheckCollision(View v1,View v2) {
        Rect R1=new Rect(v1.getLeft(), v1.getTop(), v1.getRight(), v1.getBottom());
        Rect R2=new Rect(v2.getLeft(), v2.getTop(), v2.getRight(), v2.getBottom());
        return R1.intersect(R2);
    }



    public void buttonEndTurn(View view){
        currentBoard.endTurn();
        getTimerInstance().cancel();
        internalTimer = null;
        roundOrganize(false);
        //Log.d("JSON IS0","JSON:"+currentBoard.uploadBoard());
    }

    public void surrenderButton(View view){
            currentBoard.surrender();
            //invoco haiPerso
            endGame(boardView);


    }
    public void rollDiceButton(View view){
        getCurrentBoard().roll();
        ImageView roll1 = (ImageView) findViewById(R.id.diceRes1);
        ImageView roll2 = (ImageView) findViewById(R.id.diceRes2);
        roll1.setImageDrawable(getImageViewByResult(currentBoard.getDiceRes(0)));
        roll2.setImageDrawable(getImageViewByResult(currentBoard.getDiceRes(1)));
        //roll button
        findViewById(R.id.roll).setEnabled(false);
        //red pawns
        findViewById(R.id.red1).setEnabled(true);
        findViewById(R.id.red2).setEnabled(true);
        findViewById(R.id.red3).setEnabled(true);
        findViewById(R.id.red4).setEnabled(true);
        findViewById(R.id.red5).setEnabled(true);
        findViewById(R.id.red6).setEnabled(true);

        //Log.d("DICE RESULT","RES 1 "+getCurrentBoard().getDiceRes(0)+"RES 2 "+getCurrentBoard().getDiceRes(1));


    }

    private Drawable getImageViewByResult(int res){

        switch(res){
            case 1:
                return getResources().getDrawable(R.drawable.dadonum1);
            case 2:
                return getResources().getDrawable(R.drawable.dadonum2);
            case 3:
                return getResources().getDrawable(R.drawable.dadonum3);
            case 4:
                return getResources().getDrawable(R.drawable.dadonum4);
            case 5:
                return getResources().getDrawable(R.drawable.dadonum5);
            case 6:
                return getResources().getDrawable(R.drawable.dadonum6);
            default:
                return null;
        }
    }
    //todo haiVinto e haiPerso method Views
    //todo update board



    public Board getCurrentBoard() {
        return currentBoard;
    }

    public RelativeLayout getRefLayout() {
        return refLayout;
    }


    @Override
    public void onBackPressed() {//the button do nothing
    }

    public void endGame(BoardActivity ref){
        ref.getTimerInstance().cancel();
        Intent intent = new Intent(ref,
                setupPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
        ref.finish();
    }

    public void updateBoard(JSONObject json){
        //update model
        if(getCurrentBoard().updateBoard(json) ==true){
            //update views

            //red pawns update

            ViewGroup owner = (ViewGroup)findViewById(R.id.red1).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer1().getPawnbyId(1).getPosition()){
                owner.removeView(findViewById(R.id.red1));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer1().getPawnbyId(1).getPosition());
                newOwner.addView(findViewById(R.id.red1));
            }
            owner = (ViewGroup)findViewById(R.id.red2).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer1().getPawnbyId(2).getPosition()){
                owner.removeView(findViewById(R.id.red2));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer1().getPawnbyId(2).getPosition());
                newOwner.addView(findViewById(R.id.red2));
            }
            owner = (ViewGroup)findViewById(R.id.red3).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer1().getPawnbyId(3).getPosition()){
                owner.removeView(findViewById(R.id.red3));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer1().getPawnbyId(3).getPosition());
                newOwner.addView(findViewById(R.id.red3));
            }
            owner = (ViewGroup)findViewById(R.id.red4).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer1().getPawnbyId(4).getPosition()){
                owner.removeView(findViewById(R.id.red4));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer1().getPawnbyId(4).getPosition());
                newOwner.addView(findViewById(R.id.red4));
            }
            owner = (ViewGroup)findViewById(R.id.red5).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer1().getPawnbyId(5).getPosition()){
                owner.removeView(findViewById(R.id.red5));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer1().getPawnbyId(5).getPosition());
                newOwner.addView(findViewById(R.id.red5));
            }
            owner = (ViewGroup)findViewById(R.id.red6).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer1().getPawnbyId(6).getPosition()){
                owner.removeView(findViewById(R.id.red6));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer1().getPawnbyId(6).getPosition());
                newOwner.addView(findViewById(R.id.red6));
            }

            //black pawns update
            owner = (ViewGroup)findViewById(R.id.black1).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer2().getPawnbyId(1).getPosition()){
                owner.removeView(findViewById(R.id.black1));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer2().getPawnbyId(1).getPosition());
                newOwner.addView(findViewById(R.id.black1));
            }
            owner = (ViewGroup)findViewById(R.id.black2).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer2().getPawnbyId(2).getPosition()){
                owner.removeView(findViewById(R.id.black2));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer2().getPawnbyId(2).getPosition());
                newOwner.addView(findViewById(R.id.black2));
            }
            owner = (ViewGroup)findViewById(R.id.black3).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer2().getPawnbyId(3).getPosition()){
                owner.removeView(findViewById(R.id.black3));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer2().getPawnbyId(3).getPosition());
                newOwner.addView(findViewById(R.id.black3));
            }
            owner = (ViewGroup)findViewById(R.id.black4).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer2().getPawnbyId(4).getPosition()){
                owner.removeView(findViewById(R.id.black4));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer2().getPawnbyId(4).getPosition());
                newOwner.addView(findViewById(R.id.black4));
            }
            owner = (ViewGroup)findViewById(R.id.black5).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer2().getPawnbyId(5).getPosition()){
                owner.removeView(findViewById(R.id.black5));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer2().getPawnbyId(5).getPosition());
                newOwner.addView(findViewById(R.id.black5));
            }
            owner = (ViewGroup)findViewById(R.id.black6).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getCurrentBoard().getPlayer2().getPawnbyId(6).getPosition()){
                owner.removeView(findViewById(R.id.black6));
                RelativeLayout newOwner = findCellByIndex(getCurrentBoard().getPlayer2().getPawnbyId(6).getPosition());
                newOwner.addView(findViewById(R.id.black6));
            }


        }
        else{
            currentBoard.surrender();
            endGame(boardView);
        }



    }

    public RelativeLayout findCellByIndex(int v){


        switch(v) {
            case 0:
                return (RelativeLayout)findViewById(R.id.cell1L);
            case 1:
                return (RelativeLayout)findViewById(R.id.cell2L);
            case 2:
                return (RelativeLayout)findViewById(R.id.cell3L);
            case 3:
                return (RelativeLayout)findViewById(R.id.cell4L);
            case 4:
                return (RelativeLayout)findViewById(R.id.cell5L);
            case 5:
                return (RelativeLayout)findViewById(R.id.cell6L);
            case 6:
                return (RelativeLayout)findViewById(R.id.cell7L);
            case 7:
                return (RelativeLayout)findViewById(R.id.cell8L);
            case 8:
                return (RelativeLayout)findViewById(R.id.cell9L);
            case 9:
                return (RelativeLayout)findViewById(R.id.cell10L);
            case 10:
                return (RelativeLayout)findViewById(R.id.cell11L);
            case 11:
                return (RelativeLayout)findViewById(R.id.cell12L);
            case 12:
                return (RelativeLayout)findViewById(R.id.cell13L);
            case 13:
                return (RelativeLayout)findViewById(R.id.cell14L);
            case 14:
                return (RelativeLayout)findViewById(R.id.cell15L);
            case 15:
                return (RelativeLayout)findViewById(R.id.cell16L);
            case 16:
                return (RelativeLayout)findViewById(R.id.cell17L);
            case 17:
                return (RelativeLayout)findViewById(R.id.cell18L);
            case 18:
                return (RelativeLayout)findViewById(R.id.cell19L);
            case 19:
                return (RelativeLayout)findViewById(R.id.cell20L);
            case 20:
                return (RelativeLayout)findViewById(R.id.cell21L);
            case 21:
                return (RelativeLayout)findViewById(R.id.cell22L);
            case 22:
                return (RelativeLayout)findViewById(R.id.cell23L);
            case 23:
                return (RelativeLayout)findViewById(R.id.cell24L);
            case 24:
                return (RelativeLayout)findViewById(R.id.cell25L);
            case 25:
                return (RelativeLayout)findViewById(R.id.cell26L);
            case 26:
                return (RelativeLayout)findViewById(R.id.cell27L);
            case 27:
                return (RelativeLayout)findViewById(R.id.cell28L);
            case 28:
                return (RelativeLayout)findViewById(R.id.cell29L);
            case 29:
                return (RelativeLayout)findViewById(R.id.cell30L);
            case 30:
                return (RelativeLayout)findViewById(R.id.cell31L);
            default:
                return null;

        }

    }
}
