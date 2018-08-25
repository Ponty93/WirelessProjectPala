package com.project.wirelessteam.wirelessproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
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
public class BoardActivity extends AppCompatActivity implements View.OnTouchListener {
    private Board currentBoard;
    private int _xDelta;
    private int _yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent buildBoard = getIntent();
        TextView idPl = (TextView) findViewById(R.id.idAvv);
        TextView userPl = (TextView) findViewById(R.id.userAvv);
        idPl.setText(Integer.toString(buildBoard.getIntExtra("player2Id",0)));
        userPl.setText(buildBoard.getStringExtra("player2Name"));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        int id1 = buildBoard.getIntExtra("player1Id", 0);
        int id2 = buildBoard.getIntExtra("player2Id", 0);
        //int gameId = buildBoard.getIntExtra("gameId", 0);
        String user1 = buildBoard.getStringExtra("player1Name");
        String user2 = buildBoard.getStringExtra("player2Name");
        boolean roundOrder = true;// settleFirst();
        currentBoard = new Board(30, new Player(roundOrder, id1, user1), new Player(!roundOrder, id2, user2), 1);
        setPawnView(this,currentBoard.getPlayer1().getPawns(),"player1");
        setPawnView(this,currentBoard.getPlayer2().getPawns(),"player2");

        //Sets the player1 pawns able to move
        findViewById(R.id.red1).setOnTouchListener(this);
        findViewById(R.id.red2).setOnTouchListener(this);
        findViewById(R.id.red3).setOnTouchListener(this);
        findViewById(R.id.red4).setOnTouchListener(this);
        findViewById(R.id.red5).setOnTouchListener(this);
        findViewById(R.id.red6).setOnTouchListener(this);

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


   @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                        view.getLayoutParams();

                _xDelta = x - lParams.leftMargin;
                _yDelta = y - lParams.topMargin;
                break;
            //case when i lift my finger off the pawn
            case MotionEvent.ACTION_UP:
                Toast.makeText(BoardActivity.this,
                        "RILASCIATO DITINO!", Toast.LENGTH_SHORT)
                        .show();
                //Boolean collision= CheckCollision(view,bImageView);
                break;
            //case when pawn moves
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = x - _xDelta;
                layoutParams.topMargin = y - _yDelta;
                layoutParams.rightMargin = 0;
                layoutParams.bottomMargin = 0;
                view.setLayoutParams(layoutParams);
                break;

        }
        findViewById(R.id.rel1).invalidate();

        return true;
    }

    public boolean CheckCollision(View v1,View v2) {
        Rect R1=new Rect(v1.getLeft(), v1.getTop(), v1.getRight(), v1.getBottom());
        Rect R2=new Rect(v2.getLeft(), v2.getTop(), v2.getRight(), v2.getBottom());
        return R1.intersect(R2);
    }

    public boolean onDrag(View view, DragEvent dragEvent) {
        return false;
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
