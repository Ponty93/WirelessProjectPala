package com.project.wirelessteam.wirelessproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;

import Actors.Pawn;
import Actors.Player;
import GameLogic.Board;
import GameLogic.Game;
import API.phpConnect;
import Utils.onTouchCustomMethod;
import Utils.onTouchCustomMethod;
import Utils.onDragCustomMethod;


public class BoardActivity extends AppCompatActivity {
    private Board currentBoard = null;
    private Context context = null;
    private RelativeLayout refLayout=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent buildBoard = getIntent();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        init(buildBoard);
        //todo send a req to reload the data if any
    }

    private void init(Intent buildBoard) {

        if(context == null)
            context = getApplicationContext();

        //init the board and the retrieve the data for player 2
        TextView idPl = (TextView) findViewById(R.id.idAvv);
        TextView userPl = (TextView) findViewById(R.id.userAvv);
        idPl.setText(Integer.toString(buildBoard.getIntExtra("player2Id",0)));
        userPl.setText(buildBoard.getStringExtra("player2Name"));
        int id1 = buildBoard.getIntExtra("player1Id", 0);
        int id2 = buildBoard.getIntExtra("player2Id", 0);
        //int gameId = buildBoard.getIntExtra("gameId", 0);
        String user1 = buildBoard.getStringExtra("player1Name");
        String user2 = buildBoard.getStringExtra("player2Name");
        boolean roundOrder = true;// settleFirst();

        if(currentBoard == null) {
            currentBoard = new Board(30, new Player(roundOrder, id1, user1), new Player(!roundOrder, id2, user2), 1);
            setPawnView(context, currentBoard.getPlayer1().getPawns(), "player1");
            setPawnView(context, currentBoard.getPlayer2().getPawns(), "player2");
        }

        if(refLayout == null){
            refLayout = (RelativeLayout) findViewById(R.id.rel1);
        }

        //Sets the player1 pawns able to move and their tag
        ImageView red1 = findViewById(R.id.red1);
        red1.setOnTouchListener(new onTouchCustomMethod(context,refLayout));
        red1.setTag("red1");

        ImageView red2 = findViewById(R.id.red2);
        red2.setOnTouchListener(new onTouchCustomMethod(context,refLayout));
        red2.setTag("red2");

        ImageView red3 = findViewById(R.id.red3);
        red3.setOnTouchListener(new onTouchCustomMethod(context,refLayout));
        red3.setTag("red3");

        ImageView red4 = findViewById(R.id.red4);
        red4.setOnTouchListener(new onTouchCustomMethod(context,refLayout));
        red4.setTag("red4");

        ImageView red5 = findViewById(R.id.red5);
        red5.setOnTouchListener(new onTouchCustomMethod(context,refLayout));
        red5.setTag("red5");

        ImageView red6 = findViewById(R.id.red6);
        red6.setOnTouchListener(new onTouchCustomMethod(context,refLayout));
        red6.setTag("red6");

        //sets the cell able to receive the pawns
        findViewById(R.id.cell1).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell2).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell3).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell4).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell5).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell6).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell7).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell8).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell9).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell10).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell11).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell12).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell13).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell14).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell15).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell16).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell17).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell18).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell19).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell20).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell21).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell22).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell23).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell24).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell25).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell26).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell27).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell28).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell29).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell30).setOnDragListener(new onDragCustomMethod(context));
        findViewById(R.id.cell31).setOnDragListener(new onDragCustomMethod(context));
    }

    private void setPawnView(Context context,HashMap<Integer, Pawn> map, String player){
        if(player.equals("player1")) {
            map.get(0).setPawnView((ImageView)findViewById(R.id.red1));
            map.get(1).setPawnView((ImageView)findViewById(R.id.red2));
            map.get(2).setPawnView((ImageView)findViewById(R.id.red3));
            map.get(3).setPawnView((ImageView)findViewById(R.id.red4));
            map.get(4).setPawnView((ImageView)findViewById(R.id.red5));
            map.get(5).setPawnView((ImageView)findViewById(R.id.red6));


        }
        else {
            map.get(0).setPawnView((ImageView)findViewById(R.id.black1));
            map.get(1).setPawnView((ImageView)findViewById(R.id.black2));
            map.get(2).setPawnView((ImageView)findViewById(R.id.black3));
            map.get(3).setPawnView((ImageView)findViewById(R.id.black4));
            map.get(4).setPawnView((ImageView)findViewById(R.id.black5));
            map.get(5).setPawnView((ImageView)findViewById(R.id.black6));
        }
    }




    public boolean CheckCollision(View v1,View v2) {
        Rect R1=new Rect(v1.getLeft(), v1.getTop(), v1.getRight(), v1.getBottom());
        Rect R2=new Rect(v2.getLeft(), v2.getTop(), v2.getRight(), v2.getBottom());
        return R1.intersect(R2);
    }



    /**
     * method to settle who starts first
     * Returns a boolean that rappresents the result
     * evens: true;
     * odds = false;
     * @return {boolean}
     */

    /*public boolean settleFirst() {
        int intResult = (int)Math.random()*6+1;
        //todo game.php roundOrder
        phpConnect tmpConn = new phpConnect("https://www.psionofficial.com/Wireless/handler.php",-1);
        tmpConn.execute("r","GAME","-1","diceValue",Integer.toString(intResult));
        return Boolean.parseBoolean(tmpConn.getParamFromJson("roundOrder"));
    }*/


}
