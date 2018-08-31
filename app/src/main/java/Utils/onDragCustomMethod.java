package Utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
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

                    if(dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                        view.setBackgroundColor(Color.BLUE);
                        view.invalidate();
                        return true;
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

                int cellNumber = Integer.parseInt((String)newParent.getTag());
                if(cellNumber >6 && cellNumber !=30)
                    setPawnPositionInCell(srcView,newParent);

                currentBoard.getPlayer1().setPawnPosition(findViewById(srcView),Integer.parseInt((String)newParent.getTag()));
                currentBoard.setNumberOfMove(currentBoard.getNumberOfMove()+1);
                return true;


            case DragEvent.ACTION_DRAG_ENDED:
                view.setBackgroundColor(Color.WHITE);
                view.invalidate();
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

    private int findCellByTag(View v){
        String cell = (String)v.getTag();

        switch(cell) {
            case "0":
                return 0;
            case "1":
                return 1;
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            case "6":
                return 6;
            case "7":
                return 7;
            case "8":
                return 8;
            case "9":
                return 9;
            case "10":
                return 10;
            case "11":
                return 11;
            case "12":
                return 12;
            case "13":
                return 13;
            case "14":
                return 14;
            case "15":
                return 15;
            case "16":
                return 16;
            case "17":
                return 17;
            case "18":
                return 18;
            case "19":
                return 19;
            case "20":
                return 20;
            case "21":
                return 21;
            case "22":
                return 22;
            case "23":
                return 23;
            case "24":
                return 24;
            case "25":
                return 25;
            case "26":
                return 26;
            case "27":
                return 27;
            case "28":
                return 28;
            case "29":
                return 29;
            case "30":
                return 30;
            default:
                return -1;

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
}
