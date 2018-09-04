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
import android.widget.Toast;

import com.project.wirelessteam.Views.BoardActivity;
import com.project.wirelessteam.Views.R;

import Model.Board;

public class onDragCustomMethod implements View.OnDragListener {
    private BoardActivity refActivity;
    private Board currentBoard;
    //var to be used in onDrag method
    private int local = 0;
    private int action1=0,action2=0,action3=0;


    public onDragCustomMethod(BoardActivity boardAct, Board boardRef)
    {
        refActivity = boardAct;
        currentBoard = boardRef;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent){
        int action = dragEvent.getAction();


        switch(action) {
            case DragEvent.ACTION_DRAG_STARTED:
                local = findPositionByView((View) dragEvent.getLocalState());
                action1 = currentBoard.getDiceRes(0);
                action2 = currentBoard.getDiceRes(1);
                action3 = action1 + action2;

                if(local+action1 != local || local + action2 != local || local+action3 != local) {
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        if (Integer.parseInt((String) view.getTag()) == local + action1 && local + action1 != 0) {
                            refActivity.findCellByIndex(local + action1).setBackgroundColor(Color.RED);
                            return true;
                        }
                        if (Integer.parseInt((String) view.getTag()) == local + action2 && local + action2 != 0) {
                            refActivity.findCellByIndex(local + action2).setBackgroundColor(Color.RED);
                            return true;
                        }
                        if (Integer.parseInt((String) view.getTag()) == local + action3 && local + action3 != 0) {
                            refActivity.findCellByIndex(local + action3).setBackgroundColor(Color.RED);
                            return true;
                        }
                    }
                }

                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                view.setBackgroundColor(Color.BLUE);
                view.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            //not doing anything
            case DragEvent.ACTION_DRAG_EXITED:
                view.setBackgroundColor(Color.WHITE);
                view.invalidate();
                return true;
            case DragEvent.ACTION_DROP:
                ClipData data = dragEvent.getClipData();
                ClipData.Item item = data.getItemAt(0);
                String itemText = item.getText().toString();
                Toast.makeText(refActivity,"Dragged data is"+ itemText,Toast.LENGTH_LONG).show();
                resetTargetViewBackground(view);
                view.invalidate();


                // Get dragged view object from drag event object.
                View srcView = (View)dragEvent.getLocalState();
                // Get dragged view's parent view group.
                ViewGroup owner = (ViewGroup) srcView.getParent();
                // Remove source view from original parent view group.

                    owner.removeView(srcView);

                    RelativeLayout newParent = (RelativeLayout) view;


                    int cellNumber = Integer.parseInt((String) newParent.getTag());

                    if (cellNumber > 6 && cellNumber != 30)
                        setPawnPositionInCell(srcView, newParent);

                    //todo risolvere problema
                    if (cellNumber == local + action1) {
                       /* if((cellNumber>6 && cellNumber!=30) && isBlack(newParent.getChildAt(1))){
                            if(currentBoard.canEat(currentBoard.getPlayer1().getUserId(),currentBoard.getPlayer2().getUserId(),local,local+action1)){
                                currentBoard.eatPawn(currentBoard.getPlayer2().getUserId(),local+action1);
                            }
                        }
                        currentBoard.movePawn(currentBoard.getPlayer1().getUserId(),currentBoard.getPlayer1().getPawnbyId(findViewByTag(srcView)).getIdDB(),local,local+action1);
                        */currentBoard.setDiceResToNullInPos(0);
                        currentBoard.setNumberOfMove(currentBoard.getNumberOfMove() + 1);
                    }
                    else if(cellNumber == local + action2) {
                        /*if((cellNumber>6 && cellNumber!=30) && isBlack(newParent.getChildAt(1))){
                            if(currentBoard.canEat(currentBoard.getPlayer1().getUserId(),currentBoard.getPlayer2().getUserId(),local,local+action2)){
                                currentBoard.eatPawn(currentBoard.getPlayer2().getUserId(),local+action2);
                                //todo

                            }
                        }
                        currentBoard.movePawn(currentBoard.getPlayer1().getUserId(),currentBoard.getPlayer1().getPawnbyId(findViewByTag(srcView)).getIdDB(),local,local+action2);
                        */currentBoard.setDiceResToNullInPos(1);
                        currentBoard.setNumberOfMove(currentBoard.getNumberOfMove() + 1);
                    }
                    else if (cellNumber == local + action3) {
                        /*if((cellNumber>6 && cellNumber!=30) && isBlack(newParent.getChildAt(1))){
                            if(currentBoard.canEat(currentBoard.getPlayer1().getUserId(),currentBoard.getPlayer2().getUserId(),local,local+action3)){
                                currentBoard.eatPawn(currentBoard.getPlayer2().getUserId(),local+action3);
                            }
                        }
                        currentBoard.movePawn(currentBoard.getPlayer1().getUserId(),currentBoard.getPlayer1().getPawnbyId(findViewByTag(srcView)).getIdDB(),local,local+action3);
                        */currentBoard.setDiceResToNull();
                        currentBoard.setNumberOfMove(currentBoard.getNumberOfMove() + 2);
                    }

                    newParent.addView(srcView);

                    currentBoard.getPlayer1().setPawnPosition(findViewByTag(srcView), Integer.parseInt((String) newParent.getTag()));
                    return true;





            case DragEvent.ACTION_DRAG_ENDED:
                ((RelativeLayout) view).setBackgroundColor(Color.WHITE);
                view.invalidate();
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
        // Clear color filter.
        /*if(view.getTag().equals("cell1L") || view.getTag().equals("cell31L"))
            view.setBackgroundColor(Color.BLUE);
        else if(view.getTag().equals("cell2L") || view.getTag().equals("cell3L") || view.getTag().equals("cell4L") || view.getTag().equals("cell5L")
            || view.getTag().equals("cell6L") || view.getTag().equals("cell7L")) {
            view.setBackgroundColor(Color.GREEN);
        }
        else*/
            view.getBackground().clearColorFilter();
        // Redraw the target view use original color.
        view.invalidate();
    }

    private int findPositionByView(View view){
        String tag = (String) view.getTag();

        switch(tag){
            case "red1":
                return currentBoard.getPlayer1().getPawnbyId(1).getPosition();
            case "red2":
                return currentBoard.getPlayer1().getPawnbyId(2).getPosition();
            case "red3":
                return currentBoard.getPlayer1().getPawnbyId(3).getPosition();
            case "red4":
                return currentBoard.getPlayer1().getPawnbyId(4).getPosition();
            case "red5":
                return currentBoard.getPlayer1().getPawnbyId(5).getPosition();
            case "red6":
                return currentBoard.getPlayer1().getPawnbyId(6).getPosition();
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
}
