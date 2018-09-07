package Utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.wirelessteam.Views.BoardActivity;
import com.project.wirelessteam.Views.R;

import java.util.ArrayList;

import Controller.boardController;
import Model.Board;

public class onDragCustomMethod implements View.OnDragListener {
    private BoardActivity refActivity;
    private boardController controller;
    //var to be used in onDrag method
    private int local = 0;
    private int action1=0,action2=0,action3=0;



    public onDragCustomMethod(BoardActivity boardAct, boardController boardRef)
    {
        refActivity = boardAct;
        controller = boardRef;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent){
        int action = dragEvent.getAction();

        RelativeLayout newParent = (RelativeLayout) view;
        int cellNumber = Integer.parseInt((String) newParent.getTag());

        switch(action) {
            case DragEvent.ACTION_DRAG_STARTED:
                local = findPositionByView((View) dragEvent.getLocalState());
                action1 = controller.getDiceRes(0);
                action2 = controller.getDiceRes(1);
                action3 = action1 + action2;



                if((local+action1 != local || local + action2 != local || local+action3 != local) && Integer.parseInt((String) view.getTag()) != local) {
                    if(dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        if(Integer.parseInt((String) view.getTag()) == local + action1 && local + action1 != 0) {
                            refActivity.findCellByIndex(local + action1).setBackgroundColor(Color.RED);
                            if (cellNumber > 6 && isOccupied(newParent))
                                if (isBlack(newParent.getChildAt(1)))
                                    return controller.canEat(controller.getPlayer1().getUserId(), controller.getPlayer2().getUserId(), local, local + action1);

                            return true;
                        }
                        if (Integer.parseInt((String) view.getTag()) == local + action2 && local + action2 != 0) {
                            refActivity.findCellByIndex(local + action2).setBackgroundColor(Color.RED);
                            if (cellNumber > 6 && isOccupied(newParent))
                                if (isBlack(newParent.getChildAt(1)))
                                    return controller.canEat(controller.getPlayer1().getUserId(), controller.getPlayer2().getUserId(), local, local + action2);

                            return true;
                        }
                        if (Integer.parseInt((String) view.getTag()) == local + action3 && local + action3 != 0) {
                            refActivity.findCellByIndex(local + action3).setBackgroundColor(Color.RED);
                            if (cellNumber > 6 && isOccupied(newParent))
                                if (isBlack(newParent.getChildAt(1)))
                                    return controller.canEat(controller.getPlayer1().getUserId(), controller.getPlayer2().getUserId(), local, local + action3);

                            return true;
                        }
                    }
                }

                return false;

            case DragEvent.ACTION_DRAG_ENTERED:

                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            //not doing anything
            case DragEvent.ACTION_DRAG_EXITED:
                return true;
            case DragEvent.ACTION_DROP:
                ClipData data = dragEvent.getClipData();
                ClipData.Item item = data.getItemAt(0);
                String itemText = item.getText().toString();

                int reds= controller.howManyPawns(local,controller.getPlayer1().getUserId());
                ArrayList<View> stack = new ArrayList<>();


                resetTargetViewBackground(view);
                view.invalidate();


                // Get dragged view object from drag event object.
                View srcView = (View)dragEvent.getLocalState();

                // Get dragged view's parent view group.
                ViewGroup owner = (ViewGroup) srcView.getParent();
                // Remove source view from original parent view group.


                if(reds>1 && ((String)owner.getTag()).equals("0") == false)
                    stack = loadArray(owner,reds);

                owner.removeView(srcView);




                //todo not functioning properly
                if (cellNumber > 6 && cellNumber != 30)
                    setPawnPositionInCell(srcView, newParent);

                boolean pawnWin = false;

                if (cellNumber == local + action1) {
                    if(controller.checkIfPawnWin(local + action1)) {
                        controller.getPlayer1().setScore(controller.getPlayer1().getScore()+controller.howManyPawns(local,controller.getPlayer1().getUserId()));
                        ((TextView)refActivity.findViewById(R.id.scorePL)).setText(Integer.toString(controller.getPlayer1().getScore()));

                        if(controller.getPlayer1().getScore() == 6)
                            //todo hai vinto se ==6
                        pawnWin = true;
                    }
                    else if(cellNumber>6 && isOccupied(newParent)){
                            if(isBlack(newParent.getChildAt(1))){
                                if(controller.canEat(controller.getPlayer1().getUserId(),controller.getPlayer2().getUserId(),local,local+action1)){
                                    int blackStack = controller.howManyPawns(local+action1,controller.getPlayer2().getUserId());
                                    int index=0;
                                    while(index<blackStack) {
                                        controller.eatPawn(controller.getPlayer2().getPawnbyId(findViewByTag(newParent.getChildAt(1))).getId());
                                        View black = (View) newParent.getChildAt(1);
                                        newParent.removeView(black);
                                        refToStart().addView(black);
                                        index++;
                                    }
                                }
                            }
                        }
                    Toast.makeText(refActivity,"Dragged data is"+itemText+" with "+reds+" on "+cellNumber+" with "+ controller.howManyPawns(local+action1,controller.getPlayer1().getUserId())+" in "+view.getTag(),Toast.LENGTH_LONG).show();

                    controller.setDiceResToNullInPos(0);
                        controller.setNumberOfMove(controller.getNumberOfMove() + 1);
                    }
                    else if(cellNumber == local + action2) {
                    if(controller.checkIfPawnWin(local + action2)) {
                        controller.getPlayer1().setScore(controller.getPlayer1().getScore()+controller.howManyPawns(local,controller.getPlayer1().getUserId()));
                        ((TextView)refActivity.findViewById(R.id.scorePL)).setText(Integer.toString(controller.getPlayer1().getScore()));
                        pawnWin = true;
                    }
                    else if(cellNumber>6 && isOccupied(newParent)){
                            if(isBlack(newParent.getChildAt(1))){
                            if(controller.canEat(controller.getPlayer1().getUserId(),controller.getPlayer2().getUserId(),local,local+action2)){
                                int blackStack = controller.howManyPawns(local+action2,controller.getPlayer2().getUserId());
                                int index=0;
                                while(index<blackStack) {
                                    controller.eatPawn(controller.getPlayer2().getPawnbyId(findViewByTag(newParent.getChildAt(1))).getId());
                                    View black = (View) newParent.getChildAt(1);
                                    newParent.removeView(black);
                                    refToStart().addView(black);
                                    index++;
                                }
                            }
                            }
                        }
                    Toast.makeText(refActivity,"Dragged data is "+itemText+" with "+reds+" with "+ controller.howManyPawns(local+action2,controller.getPlayer2().getUserId())+" in "+cellNumber,Toast.LENGTH_LONG).show();

                    controller.setDiceResToNullInPos(1);
                        controller.setNumberOfMove(controller.getNumberOfMove() + 1);
                    }
                    else if (cellNumber == local + action3) {
                    if(controller.checkIfPawnWin(local + action3)) {
                        controller.getPlayer1().setScore(controller.getPlayer1().getScore()+controller.howManyPawns(local,controller.getPlayer1().getUserId()));
                        ((TextView)refActivity.findViewById(R.id.scorePL)).setText(Integer.toString(controller.getPlayer1().getScore()));
                        pawnWin = true;
                    }
                    else if(cellNumber>6  && isOccupied(newParent)){
                        if(isBlack(newParent.getChildAt(1))){
                            if(controller.canEat(controller.getPlayer1().getUserId(),controller.getPlayer2().getUserId(),local,local+action3)){
                                int blackStack = controller.howManyPawns(local+action3,controller.getPlayer2().getUserId());
                                int index=0;
                                while(index<blackStack) {
                                    controller.eatPawn(controller.getPlayer2().getPawnbyId(findViewByTag(newParent.getChildAt(1))).getId());
                                    View black = (View) newParent.getChildAt(1);
                                    newParent.removeView(black);
                                    refToStart().addView(black);
                                    index++;
                                }
                            }
                        }
                    }
                    Toast.makeText(refActivity,"Dragged data is"+itemText+" with "+reds+" on "+cellNumber+" with "+ controller.howManyPawns(local+action3,controller.getPlayer1().getUserId())+" in "+view.getTag(),Toast.LENGTH_LONG).show();

                    controller.setDiceResToNull();
                        controller.setNumberOfMove(controller.getNumberOfMove() + 2);
                    }

                    newParent.addView(srcView);
                    controller.movePawn(findViewByTag(srcView), Integer.parseInt((String) newParent.getTag()));

                    if(((String) owner.getTag()).equals("0") == false)
                        removeFromArray(stack,newParent);

                    if(pawnWin == true){
                            srcView.setEnabled(false);
                            disableWinnerPawns(stack);
                    }

                    return true;

            case DragEvent.ACTION_DRAG_ENDED:
                resetTargetViewBackground(view);

                if(dragEvent.getResult())
                    Toast.makeText(refActivity,"The drop was handled",Toast.LENGTH_LONG);
                else
                    Toast.makeText(refActivity,"The drop was not handled",Toast.LENGTH_LONG);

                return true;

            default:
                Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                break;

        }
        return false;

    }

    private int findViewByTag(View v){
        String pawn = (String)v.getTag();

        switch(pawn) {
            case "red1":
                return 1;
            case "red2":
                return 2;
            case "red3":
                return 3;
            case "red4":
                return 4;
            case "red5":
                return 5;
            case "red6":
                return 6;
            case "black1":
                return 1;
            case "black2":
                return 2;
            case "black3":
                return 3;
            case "black4":
                return 4;
            case "black5":
                return 5;
            case "black6":
                return 6;
            default:
                return 0;

        }

    }



   public void setPawnPositionInCell(View v,RelativeLayout layout){
        RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams) v.getLayoutParams();

            params.addRule(RelativeLayout.RIGHT_OF,0);
            params.addRule(RelativeLayout.LEFT_OF,0);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL,1);
            params.addRule(RelativeLayout.CENTER_VERTICAL,1);
            params.addRule(RelativeLayout.CENTER_IN_PARENT,1);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);

    }
    private void resetTargetViewBackground(View view)
    {

        view.setBackground(refActivity.getResources().getDrawable(R.drawable.mar3));
        // Redraw the target view use original color.
        view.invalidate();
    }

    private int findPositionByView(View view){
        String tag = (String) view.getTag();

        switch(tag){
            case "red1":
                return controller.getPlayer1().getPawnbyId(1).getPosition();
            case "red2":
                return controller.getPlayer1().getPawnbyId(2).getPosition();
            case "red3":
                return controller.getPlayer1().getPawnbyId(3).getPosition();
            case "red4":
                return controller.getPlayer1().getPawnbyId(4).getPosition();
            case "red5":
                return controller.getPlayer1().getPawnbyId(5).getPosition();
            case "red6":
                return controller.getPlayer1().getPawnbyId(6).getPosition();
            default:
                return 0;
        }
    }

    public boolean isBlack(View v){
        String tag = (String)v.getTag();

        switch(tag){
            case "black1":
                return true;
            case "black2":
                return true;
            case "black3":
                return true;
            case "black4":
                return true;
            case "black5":
                return true;
            case "black6":
                return true;
            default:
                return false;
        }
    }

    public RelativeLayout refToStart(){
        return refActivity.findViewById(R.id.cell1L);
    }

    public ArrayList<View> loadArray(ViewGroup owner,int howMuch){
        ArrayList<View> stack = new ArrayList<>();

        int index=0;
        while(index<howMuch-1){
            stack.add(owner.getChildAt(1));
            owner.removeView(owner.getChildAt(1));
            index++;
        }
        return stack;
    }

    public void removeFromArray(ArrayList<View> stack,ViewGroup newParent){
        int howMuch = stack.size();
        int index =0;
        while(index < howMuch){
            newParent.addView(stack.get(index));
            controller.movePawn(findViewByTag(stack.get(index)), Integer.parseInt((String) newParent.getTag()));
            index++;
        }
    }

    public boolean isOccupied(ViewGroup v){
        if(v.getChildAt(1) != null)
            return true;
        else
            return false;
    }

    public void disableWinnerPawns(ArrayList<View> wins){
        int howMuch = wins.size();
        int index = 0;
        while(index<howMuch){
            wins.get(index).setEnabled(false);
            index++;
        }
    }
}
