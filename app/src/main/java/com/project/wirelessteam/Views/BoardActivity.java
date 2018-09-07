package com.project.wirelessteam.Views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
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
import Controller.boardController;
import Model.Board;
import API.phpConnect;
import Utils.onTouchCustomMethod;
import Utils.onDragCustomMethod;


public class BoardActivity extends AppCompatActivity {
    private Context context = null;
    private RelativeLayout refLayout=null;
    private final BoardActivity boardView = this;
    private Timer internalTimer;
    private boardController controller = null;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent buildBoard = getIntent();
        mp = MediaPlayer.create(this, R.raw.menu);
        mp.start();
        mp.setLooping(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //initialize the boards view feature
        init(buildBoard);

    }



    protected class roundTimeout extends TimerTask {
        private int counter = 0;
        private BoardActivity refBoard;

        roundTimeout(BoardActivity board) {
            refBoard = board;
        }

        @Override
        public void run() {
            if(getController().getNumberOfMove()==2){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.endTurn).setEnabled(true);
                    }
                });
            }

            counter++;

            if(counter == 40){
                controller.surrender();
                refBoard.internalTimer.cancel();
                endGame(refBoard);
            }


            Log.d("COUNTER END ROUND","COUNTER IS:"+counter);
            JSONObject res = getController().updateRound();
            //Log.d("JSON", "Json res" + res));
            if(res != null) {
                try {
                    if (res.getInt("Result") == 1) {
                        if (res.getString("winner").equals("none") == false) {
                            if (res.getInt("winner") == getController().getPlayer1().getUserId()) {
                                refBoard.internalTimer.cancel();
                                endGame(refBoard);
                                //todo display hai vinto
                            }
                        }
                    }
                }catch(JSONException e){e.printStackTrace();}
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
            final JSONObject res = getController().updateRound();
            Log.d("JSON","Json res"+res);
            if(res!=null) {
                try {
                    if (res.getInt("Result") == 1) {
                        if (res.getString("winner").equals("none") == false) {
                            if (res.getInt("winner") == refBoard.getController().getPlayer1().getUserId()) {
                                refBoard.internalTimer.cancel();
                                endGame(refBoard);
                                //todo display hai vinto
                            } else {
                                //todo hai perso : avversario è arrivato con i 6 pawn al finish
                            }
                        } else {
                            refBoard.internalTimer.cancel();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateBoard(res);
                                    roundOrganize(true);
                                }
                            });

                        }

                    }
                }catch(JSONException e){e.printStackTrace();}
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

            findViewById(R.id.red1).setEnabled(false);
            findViewById(R.id.red2).setEnabled(false);
            findViewById(R.id.red3).setEnabled(false);
            findViewById(R.id.red4).setEnabled(false);
            findViewById(R.id.red5).setEnabled(false);
            findViewById(R.id.red6).setEnabled(false);


        //increments the players rounds


        findViewById(R.id.roll).setEnabled(round);
        //display a message "waiting for foe to finish his turn.."

        //increments the players rounds
        // sets to 0 the number of move in this round
        // sets the foe score
        if(round == true) {
            getController().setCounter(getController().getRoundsCounter() + 1);
            controller.setDiceResToNull();
            controller.setNumberOfMove(0);
            ((TextView)findViewById(R.id.roundNumber)).setText(Integer.toString(controller.getRoundsCounter()));
            ((TextView)findViewById(R.id.scoreAvv)).setText(Integer.toString(controller.getPlayer2().getScore()));
        }


        //timer to schedule action
        if(round == true) {//if its my turn, at 2m calls endTurn
            Log.d("internalTimer","starts my round timer");
            internalTimer = new Timer();
            internalTimer.schedule(new roundTimeout(boardView),0,3000);
        }
        else if(round == false){//its not my turn, every 10s i ask the server if its my turn now
            Log.d("internalTimer","starts NOT my round timer");
            internalTimer = new Timer();
            internalTimer.schedule(new connectionTimeout(boardView),0,3000);
        }

        findViewById(R.id.endTurn).setEnabled(false);
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



        JSONObject json=null;
        int gameId=0,roundId=0;
        try {
            json= new JSONObject(buildBoard.getStringExtra("json"));
            //Log.d("JSON INTERO","is"+json);
            //update board
            gameId =json.getInt("gameId");
            roundId = json.getInt("filetto");

            if(controller == null) {
                if(json!=null) {
                    controller = new boardController(new Board(30, new Player(id1, buildBoard.getStringExtra("player1Name"),json.getJSONObject("pawnPlayer1")), new Player(id2, buildBoard.getStringExtra("player2Name"),json.getJSONObject("pawnPlayer2")), gameId),this);
                    setPawnView(context, controller.getPlayer1().getPawns(), "player1");
                    setPawnView(context, controller.getPlayer2().getPawns(), "player2");
                }
            }

        }catch(JSONException e){
            e.printStackTrace();
        }


        //Define the player1 pawns drag and drop
        ImageView red1 = findViewById(R.id.red1);
        red1.setOnTouchListener(new onTouchCustomMethod(context,refLayout,controller));
        red1.setTag("red1");

        ImageView red2 = findViewById(R.id.red2);
        red2.setOnTouchListener(new onTouchCustomMethod(context,refLayout,controller));
        red2.setTag("red2");

        ImageView red3 = findViewById(R.id.red3);
        red3.setOnTouchListener(new onTouchCustomMethod(context,refLayout,controller));
        red3.setTag("red3");

        ImageView red4 = findViewById(R.id.red4);
        red4.setOnTouchListener(new onTouchCustomMethod(context,refLayout,controller));
        red4.setTag("red4");

        ImageView red5 = findViewById(R.id.red5);
        red5.setOnTouchListener(new onTouchCustomMethod(context,refLayout,controller));
        red5.setTag("red5");

        ImageView red6 = findViewById(R.id.red6);
        red6.setOnTouchListener(new onTouchCustomMethod(context,refLayout,controller));
        red6.setTag("red6");

        ImageView black1 = findViewById(R.id.black1);
        black1.setTag("black1");
        ImageView black2 = findViewById(R.id.black2);
        black2.setTag("black2");
        ImageView black3 = findViewById(R.id.black3);
        black3.setTag("black3");
        ImageView black4 = findViewById(R.id.black4);
        black4.setTag("black4");
        ImageView black5 = findViewById(R.id.black5);
        black5.setTag("black5");
        ImageView black6 = findViewById(R.id.black6);
        black6.setTag("black6");


        //sets the cell able to receive the pawns
        findViewById(R.id.cell1L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell1L).setTag("0");
        findViewById(R.id.cell2L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell2L).setTag("1");
        findViewById(R.id.cell3L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell3L).setTag("2");
        findViewById(R.id.cell4L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell4L).setTag("3");
        findViewById(R.id.cell5L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell5L).setTag("4");
        findViewById(R.id.cell6L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell6L).setTag("5");
        findViewById(R.id.cell7L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell7L).setTag("6");
        findViewById(R.id.cell8L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell8L).setTag("7");
        findViewById(R.id.cell9L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell9L).setTag("8");
        findViewById(R.id.cell10L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell10L).setTag("9");
        findViewById(R.id.cell11L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell11L).setTag("10");
        findViewById(R.id.cell12L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell12L).setTag("11");
        findViewById(R.id.cell13L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell13L).setTag("12");
        findViewById(R.id.cell14L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell14L).setTag("13");
        findViewById(R.id.cell15L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell15L).setTag("14");
        findViewById(R.id.cell16L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell16L).setTag("15");
        findViewById(R.id.cell17L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell17L).setTag("16");
        findViewById(R.id.cell18L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell18L).setTag("17");
        findViewById(R.id.cell19L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell19L).setTag("18");
        findViewById(R.id.cell20L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell20L).setTag("19");
        findViewById(R.id.cell21L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell21L).setTag("20");
        findViewById(R.id.cell22L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell22L).setTag("21");
        findViewById(R.id.cell23L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell23L).setTag("22");
        findViewById(R.id.cell24L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell24L).setTag("23");
        findViewById(R.id.cell25L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell25L).setTag("24");
        findViewById(R.id.cell26L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell26L).setTag("25");
        findViewById(R.id.cell27L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell27L).setTag("26");
        findViewById(R.id.cell28L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell28L).setTag("27");
        findViewById(R.id.cell29L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell29L).setTag("28");
        findViewById(R.id.cell30L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell30L).setTag("29");
        findViewById(R.id.cell31L).setOnDragListener(new onDragCustomMethod(boardView,controller));
        findViewById(R.id.cell31L).setTag("30");        //Log.d("Json ",buildBoard.getStringExtra("json"));


        if(refLayout == null){
            refLayout = (RelativeLayout) findViewById(R.id.rel1);
        }

        //abilitate to move the pawns

        roundOrganize(controller.playerOrder(roundId));

        updateBoard(json);

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



    /*
    public boolean CheckCollision(View v1,View v2) {
        Rect R1=new Rect(v1.getLeft(), v1.getTop(), v1.getRight(), v1.getBottom());
        Rect R2=new Rect(v2.getLeft(), v2.getTop(), v2.getRight(), v2.getBottom());
        return R1.intersect(R2);
    }*/



    public void buttonEndTurn(View view){
        internalTimer.cancel();
        roundOrganize(false);

        ImageView roll1 = (ImageView) findViewById(R.id.diceRes1);
        ImageView roll2 = (ImageView) findViewById(R.id.diceRes2);
        roll1.setImageDrawable(getResources().getDrawable(R.drawable.customborder));
        roll2.setImageDrawable(getResources().getDrawable(R.drawable.customborder));
        controller.endTurn();

    }

    public void surrenderButton(View view){
            controller.surrender();
            //invoco haiPerso
            mp.stop();
            boardView.internalTimer.cancel();
            endGame(boardView);


    }
    public void rollDiceButton(View view){
        getController().roll();
        ImageView roll1 = (ImageView) findViewById(R.id.diceRes1);
        ImageView roll2 = (ImageView) findViewById(R.id.diceRes2);
        roll1.setImageDrawable(getImageViewByResult(controller.getDiceRes(0)));
        roll2.setImageDrawable(getImageViewByResult(controller.getDiceRes(1)));
        //roll button
        //findViewById(R.id.roll).setEnabled(false);
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




    public boardController getController() {
        return controller;
    }

    public RelativeLayout getRefLayout() {
        return refLayout;
    }


    @Override
    public void onBackPressed() {//the button do nothing
    }

    public void endGame(BoardActivity ref){
        Intent intent = new Intent(ref,
                setupPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //find if player is winner or loser and set the drawable to loser or winner image
        //if(winner){
        //      ImageView imageView = (ImageView) findViewById(R.id.winnerimg);
        //      imageView.setImageResource(R.drawable.winner);
        //}
        //if(loser){
        //      ImageView imageView = (ImageView) findViewById(R.id.imageView1);
        //      imageView.setImageResource(R.drawable.defeat);
        //}
        //create alert dialog with commented code
        /*AlertDialog.Builder alertadd = new AlertDialog.Builder(BoardActivity.this);
        LayoutInflater inflater = LayoutInflater.from(BoardActivity.this);
        final View view = inflater.inflate(R.layout.endgamealert, null);
        alertadd.setView(view);
        alertadd.setNeutralButton("Click Here to continue!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dlg, int sumthin) {

                }
            });

        alertadd.show();*/

        startActivity(intent);
        ref.finish();
    }

    public void updateBoard(JSONObject json){
        //update model
        if(getController().updateBoard(json) == true){
            //update views

            //red pawns update

            ViewGroup owner = (ViewGroup)findViewById(R.id.red1).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer1().getPawnbyId(1).getPosition()){
                ImageView red1 = (ImageView)findViewById(R.id.red1);
                owner.removeView(findViewById(R.id.red1));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer1().getPawnbyId(1).getPosition());
                newOwner.addView(red1);
            }
            owner = (ViewGroup)findViewById(R.id.red2).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer1().getPawnbyId(2).getPosition()){
                ImageView red2 = (ImageView)findViewById(R.id.red2);
                owner.removeView(findViewById(R.id.red2));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer1().getPawnbyId(2).getPosition());
                newOwner.addView(red2);
            }
            owner = (ViewGroup)findViewById(R.id.red3).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer1().getPawnbyId(3).getPosition()){
                ImageView red3 = (ImageView)findViewById(R.id.red3);
                owner.removeView(findViewById(R.id.red3));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer1().getPawnbyId(3).getPosition());
                newOwner.addView(red3);
            }
            owner = (ViewGroup)findViewById(R.id.red4).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer1().getPawnbyId(4).getPosition()){
                ImageView red4 = (ImageView)findViewById(R.id.red4);
                owner.removeView(findViewById(R.id.red4));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer1().getPawnbyId(4).getPosition());
                newOwner.addView(red4);
            }
            owner = (ViewGroup)findViewById(R.id.red5).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer1().getPawnbyId(5).getPosition()){
                ImageView red5  = (ImageView)findViewById(R.id.red5);
                owner.removeView(findViewById(R.id.red5));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer1().getPawnbyId(5).getPosition());
                newOwner.addView(red5);
            }
            owner = (ViewGroup)findViewById(R.id.red6).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer1().getPawnbyId(6).getPosition()){
                ImageView red6  =(ImageView)findViewById(R.id.red6);
                owner.removeView(findViewById(R.id.red6));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer1().getPawnbyId(6).getPosition());
                newOwner.addView(red6);
            }

            //black pawns update
            owner = (ViewGroup)findViewById(R.id.black1).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer2().getPawnbyId(1).getPosition()){
                ImageView black1= (ImageView)findViewById(R.id.black1);
                owner.removeView(findViewById(R.id.black1));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer2().getPawnbyId(1).getPosition());
                newOwner.addView(black1);
            }
            owner = (ViewGroup)findViewById(R.id.black2).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer2().getPawnbyId(2).getPosition()){
                ImageView black2 =(ImageView)findViewById(R.id.black2);
                owner.removeView(findViewById(R.id.black2));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer2().getPawnbyId(2).getPosition());
                newOwner.addView(black2);
            }
            owner = (ViewGroup)findViewById(R.id.black3).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer2().getPawnbyId(3).getPosition()){
                ImageView black3 = (ImageView)findViewById(R.id.black3);
                owner.removeView(findViewById(R.id.black3));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer2().getPawnbyId(3).getPosition());
                newOwner.addView(black3);
            }
            owner = (ViewGroup)findViewById(R.id.black4).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer2().getPawnbyId(4).getPosition()){
                ImageView black4 = (ImageView)findViewById(R.id.black4);
                owner.removeView(findViewById(R.id.black4));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer2().getPawnbyId(4).getPosition());
                newOwner.addView(black4);
            }
            owner = (ViewGroup)findViewById(R.id.black5).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer2().getPawnbyId(5).getPosition()){
                ImageView black5 = (ImageView)findViewById(R.id.black5);
                owner.removeView(findViewById(R.id.black5));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer2().getPawnbyId(5).getPosition());
                newOwner.addView(black5);
            }
            owner = (ViewGroup)findViewById(R.id.black6).getParent();
            if(Integer.parseInt((String)owner.getTag()) != getController().getPlayer2().getPawnbyId(6).getPosition()){
                ImageView black6 = (ImageView)findViewById(R.id.black6);
                owner.removeView(findViewById(R.id.black6));
                RelativeLayout newOwner = findCellByIndex(getController().getPlayer2().getPawnbyId(6).getPosition());
                newOwner.addView(black6);
            }


        }
        else{
            controller.surrender();
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
