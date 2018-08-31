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

import Model.Board;

public class onDragCustomMethod implements View.OnDragListener {
    private Context cont = null;
    private Board currentBoard;
    public onDragCustomMethod(Context con, Board boardRef)
    {
        cont = con;
        currentBoard = boardRef;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent){
        int action = dragEvent.getAction();

        switch(action){
            case DragEvent.ACTION_DRAG_STARTED:
                int local = findPositionByView((View)dragEvent.getLocalState());
                int action1 = currentBoard.getDiceRes(0);
                int action2 = currentBoard.getDiceRes(1);
                int action3 = action1+action1;

                    if(dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                        if(Integer.parseInt((String)view.getTag()) == action1 || Integer.parseInt((String)view.getTag()) == action2 ||
                                Integer.parseInt((String)view.getTag()) == action3) {
                            ((myClass) findCellByIndex(local + action1).getChildAt(0)).setCellColor(Color.RED);
                            ((myClass) findCellByIndex(local + action2).getChildAt(0)).setCellColor(Color.RED);
                            ((myClass) findCellByIndex(local + action3).getChildAt(0)).setCellColor(Color.RED);
                            return true;
                        }
                    }

                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                view.setBackgroundColor(Color.RED);
                view.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            //not doing anything
            case DragEvent.ACTION_DRAG_EXITED:
                view.setBackgroundColor(Color.BLUE);
                view.invalidate();
                return true;
            case DragEvent.ACTION_DROP:
                ClipData data = dragEvent.getClipData();
                ClipData.Item item = data.getItemAt(0);
                String itemText = item.getText().toString();
                Toast.makeText(cont,"Dragged data is"+ itemText,Toast.LENGTH_LONG).show();
                resetTargetViewBackground(view);
                view.invalidate();


                // Get dragged view object from drag event object.
                View srcView = (View)dragEvent.getLocalState();
                // Get dragged view's parent view group.
                ViewGroup owner = (ViewGroup) srcView.getParent();
                // Remove source view from original parent view group.
                owner.removeView(srcView);

                RelativeLayout newParent = (RelativeLayout) view;
                newParent.addView(srcView);
                ((myClass)((RelativeLayout) view).getChildAt(0)).setCellColor(Color.BLACK);
                int cellNumber = Integer.parseInt((String)newParent.getTag());
                if(cellNumber >6 && cellNumber !=30)
                    setPawnPositionInCell(srcView,newParent);

                currentBoard.getPlayer1().setPawnPosition(findViewById(srcView),Integer.parseInt((String)newParent.getTag()));
                currentBoard.setNumberOfMove(currentBoard.getNumberOfMove()+1);
                return true;


            case DragEvent.ACTION_DRAG_ENDED:
                ((myClass)((RelativeLayout) view).getChildAt(0)).setCellColor(Color.BLACK);
                if(dragEvent.getResult())
                    Toast.makeText(cont,"The drop was handled",Toast.LENGTH_LONG);
                else
                    Toast.makeText(cont,"The drop was not handled",Toast.LENGTH_LONG);

                return true;

            default:
                Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                break;

        }
        return false;

    }
    private int findViewById(View v){
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

    private RelativeLayout findCellByIndex(int v){


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

    private void setPawnPositionInCell(View v,RelativeLayout layout){
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
}
