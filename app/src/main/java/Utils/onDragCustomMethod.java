package Utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.sax.RootElement;
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

        ViewGroup newParent = (ViewGroup) view;

        int cellNumber = Integer.parseInt((String) newParent.getTag());
        if(dragEvent != null) {
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    local = findPositionByView((View) dragEvent.getLocalState());
                    action1 = controller.getDiceRes(0);
                    action2 = controller.getDiceRes(1);
                    action3 = action1 + action2;


                    if ((local + action1 != local || local + action2 != local || local + action3 != local) && Integer.parseInt((String) view.getTag()) != local) {
                        if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            if (Integer.parseInt((String) view.getTag()) == local + action1 && local + action1 != 0) {
                                if (cellNumber > 0 && cellNumber < 7 && isOccupied(newParent)) {
                                    if (controller.howManyPawns(local + action1, controller.getPlayer1().getUserId()) > 0 && local == 0)
                                        return false;

                                }
                                if (cellNumber > 6) {
                                    if (controller.howManyPawns(0, controller.getPlayer1().getUserId()) > 0) {
                                        return false;
                                    } else if (isOccupied(newParent))
                                        if (refActivity.isBlack(newParent.getChildAt(1)))
                                            if (!controller.canEat(controller.getPlayer1().getUserId(), controller.getPlayer2().getUserId(), local, local + action1)) {
                                                return false;
                                            }
                                }
                                refActivity.findCellByIndex(local + action1).setBackgroundColor(Color.RED);
                                return true;
                            }
                            if (Integer.parseInt((String) view.getTag()) == local + action2 && local + action2 != 0) {
                                if (cellNumber > 0 && cellNumber < 7 && isOccupied(newParent)) {
                                    if (controller.howManyPawns(local + action2, controller.getPlayer1().getUserId()) > 0 && local == 0)
                                        return false;
                                }
                                if (cellNumber > 6) {
                                    if (controller.howManyPawns(0, controller.getPlayer1().getUserId()) > 0) {
                                        return false;
                                    } else if (isOccupied(newParent))
                                        if (refActivity.isBlack(newParent.getChildAt(1)))
                                            if (!controller.canEat(controller.getPlayer1().getUserId(), controller.getPlayer2().getUserId(), local, local + action1)) {
                                                return false;
                                            }
                                }

                                refActivity.findCellByIndex(local + action2).setBackgroundColor(Color.RED);
                                return true;
                            }
                            if (Integer.parseInt((String) view.getTag()) == local + action3 && local + action3 != 0) {
                                if (cellNumber > 0 && cellNumber < 7 && isOccupied(newParent)) {
                                    if (controller.howManyPawns(local + action3, controller.getPlayer1().getUserId()) > 0 && local == 0)
                                        return false;
                                }
                                if (cellNumber > 6) {
                                    if (controller.howManyPawns(0, controller.getPlayer1().getUserId()) > 0) {
                                        return false;
                                    } else if (isOccupied(newParent))
                                        if (refActivity.isBlack(newParent.getChildAt(1)))
                                            if (!controller.canEat(controller.getPlayer1().getUserId(), controller.getPlayer2().getUserId(), local, local + action1)) {
                                                return false;
                                            }
                                }

                                refActivity.findCellByIndex(local + action3).setBackgroundColor(Color.RED);

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

                    int reds = controller.howManyPawns(local, controller.getPlayer1().getUserId());
                    ArrayList<View> stack = new ArrayList<>();


                    view.setBackgroundColor(refActivity.getResources().getColor(R.color.cell));
                    // Redraw the target view use original color.
                    view.invalidate();



                    // Get dragged view object from drag event object.
                    View srcView = (View) dragEvent.getLocalState();

                    // Get dragged view's parent view group.
                    ViewGroup owner = (ViewGroup) srcView.getParent();
                    // Remove source view from original parent view group.


                    if (reds > 1 && ((String) owner.getTag()).equals("0") == false)
                        stack = loadArray(owner, reds);
                    else
                        owner.removeView(srcView);




                    boolean pawnWin = false;

                    if (cellNumber == local + action1) {
                        //Toast.makeText(refActivity, "Dragged data is" + itemText + " with " + reds + " on " + cellNumber + " with " + controller.howManyPawns(local + action1, controller.getPlayer1().getUserId()) + " in " + view.getTag(), Toast.LENGTH_LONG).show();

                        controller.setDiceResToNullInPos(0);
                        controller.setNumberOfMove(controller.getNumberOfMove() + 1);
                    } else if (cellNumber == local + action2) {
                        //Toast.makeText(refActivity, "Dragged data is " + itemText + " with " + reds + " with " + controller.howManyPawns(local + action2, controller.getPlayer2().getUserId()) + " in " + cellNumber, Toast.LENGTH_LONG).show();

                        controller.setDiceResToNullInPos(1);
                        controller.setNumberOfMove(controller.getNumberOfMove() + 1);
                    } else if (cellNumber == local + action3) {
                        //Toast.makeText(refActivity, "Dragged data is" + itemText + " with " + reds + " on " + cellNumber + " with " + controller.howManyPawns(local + action3, controller.getPlayer1().getUserId()) + " in " + view.getTag(), Toast.LENGTH_LONG).show();

                        controller.setDiceResToNull();
                        controller.setNumberOfMove(controller.getNumberOfMove() + 2);
                    }

                    pawnWin = checkWinner(cellNumber);
                    if (pawnWin == false && cellNumber > 6 && isOccupied(newParent))
                        eatView(newParent, cellNumber);

                    //moves just one pawn if reds == 1 else the whole stack
                    if (((String) owner.getTag()).equals("0") == false && reds > 1)
                        removeFromArray(stack, newParent);
                    else {
                        if(Integer.parseInt((String)newParent.getTag()) == 30)
                            refActivity.finishInCell(srcView);
                        else if(Integer.parseInt((String)newParent.getTag())>6)
                            refActivity.centerInCell(srcView);
                        else
                            refActivity.borderInCell(srcView);

                        newParent.addView(srcView);
                        controller.movePawn(findViewByTag(srcView), Integer.parseInt((String) newParent.getTag()));
                    }

                    if (pawnWin == true) {
                        //srcView.setEnabled(false);
                        refActivity.setPawnNumber(newParent,"",controller.getPlayer1().getUserId());
                        disableWinnerPawns();
                    }

                    if(cellNumber != 0 && cellNumber < 30) {
                        int pawnNumber = controller.howManyPawns(cellNumber,controller.getPlayer1().getUserId());
                        if(pawnNumber>1)
                            refActivity.setPawnNumber(newParent,Integer.toString(pawnNumber),controller.getPlayer1().getUserId());
                    }
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    view.setBackgroundColor(refActivity.getResources().getColor(R.color.cell));
                    // Redraw the target view use original color.
                    view.invalidate();

                    if (dragEvent.getResult())
                        Toast.makeText(refActivity, "The drop was handled", Toast.LENGTH_LONG);
                    else
                        Toast.makeText(refActivity, "The drop was not handled", Toast.LENGTH_LONG);

                    return true;

                default:
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                    break;

            }

        }
        return false;

    }

    public void eatView(ViewGroup newParent,int cellNumber) {
        if(refActivity.isBlack(newParent.getChildAt(1))){
            if(controller.canEat(controller.getPlayer1().getUserId(),controller.getPlayer2().getUserId(),local,cellNumber)){
                int blackStack = controller.howManyPawns(cellNumber,controller.getPlayer2().getUserId());
                int index=0;
                while(index<blackStack) {
                    controller.eatPawn(controller.getPlayer2().getPawnbyId(findViewByTag(newParent.getChildAt(1))).getId());
                    View black = (View) newParent.getChildAt(1);
                    newParent.removeView(black);
                    refToStart().addView(black);
                    refActivity.setPawnNumber(refToStart(),"",controller.getPlayer2().getUserId());
                    index++;
                }
            }
        }
    }

    public boolean checkWinner(int cellNumber) {
        if (controller.checkIfPawnWin(cellNumber)) {
            controller.getPlayer1().setScore(controller.getPlayer1().getScore() + controller.howManyPawns(local, controller.getPlayer1().getUserId()));
            ((TextView) refActivity.findViewById(R.id.scorePL)).setText(Integer.toString(controller.getPlayer1().getScore()));



                return true;
        }

            return false;
    }


    private int findViewByTag(View v){
        if(v != null) {
            String pawn = (String) v.getTag();

            switch (pawn) {
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
        return 0;
    }





    public boolean isInMainPath(String tag){
        if(tag == "I" || tag == "II" || tag == "III" || tag == "IV" || tag == "V" || tag == "VI")
            return true;
        else
            return false;
    }



    private int findPositionByView(View view){
        String tag = (String) view.getTag(); //bugged

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



    public RelativeLayout refToStart(){
        return refActivity.findViewById(R.id.cell1L);
    }

    public ArrayList<View> loadArray(ViewGroup owner,int howMuch){
        ArrayList<View> stack = new ArrayList<>();
        ArrayList<Integer> stackId = controller.getPlayer1().findPawnsByPos(Integer.parseInt((String)owner.getTag()));
        int index=0;
        while(index<howMuch){
            ImageView view = refActivity.findPawnViewById(stackId.get(index),controller.getPlayer1().getUserId());
            stack.add(view);
            owner.removeView(view);
            index++;
        }
        return stack;
    }

    public void removeFromArray(ArrayList<View> stack,ViewGroup newParent){
        int howMuch = stack.size();
        int index =0;
        while(index < howMuch){
            View v = stack.get(index);
            if(Integer.parseInt((String)newParent.getTag()) == 30)
                refActivity.finishInCell(v);
            else if(Integer.parseInt((String)newParent.getTag())>6)
                refActivity.centerInCell(v);
            else
                refActivity.borderInCell(v);
            newParent.addView(v);
            controller.movePawn(findViewByTag(v), Integer.parseInt((String) newParent.getTag()));
            index++;
        }
    }


    public boolean isOccupied(ViewGroup v){
        if(v.getChildAt(1) != null)
            return true;
        else
            return false;
    }

    public void disableWinnerPawns(){
        ArrayList<Integer> stack = controller.getPlayer1().findPawnsByPos(30);
        int index = 0;
        while(index<stack.size()){
            ImageView v = refActivity.findPawnViewById(stack.get(index),controller.getPlayer1().getUserId());
            v.setEnabled(false);
            index++;
        }
    }



}
