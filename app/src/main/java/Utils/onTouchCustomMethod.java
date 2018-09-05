package Utils;

import android.content.ClipData;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import Controller.boardController;
import Model.Board;

public class onTouchCustomMethod implements View.OnTouchListener {

    private RelativeLayout refLayout = null;
    private Context boardContext = null;
    private boardController controller = null;
    public onTouchCustomMethod(Context context, RelativeLayout lay, boardController ref) {
        refLayout = lay;
        boardContext = context;
        controller = ref;

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(controller.getNumberOfMove() <2) {
            ClipData dragData = ClipData.newPlainText("", (String) view.getTag());
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
            view.startDrag(dragData, shadow, view, 0);
            return true;
        }
        return false;

    }
}
