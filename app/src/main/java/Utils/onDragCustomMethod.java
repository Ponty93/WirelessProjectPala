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
