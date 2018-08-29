package Utils;

import android.content.ClipData;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import Model.Board;

public class onTouchCustomMethod implements View.OnTouchListener {

    private RelativeLayout refLayout = null;
    private Context boardContext = null;
    private Board currentBoard = null;

    public onTouchCustomMethod(Context context, RelativeLayout lay, Board ref) {
        refLayout = lay;
        boardContext = context;
        currentBoard = ref;

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(currentBoard.getNumberOfMove() <2) {
            ClipData dragData = ClipData.newPlainText("", (String) view.getTag());
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
            view.startDrag(dragData, shadow, view, 0);
            return true;
        }
        return false;

    }
}
