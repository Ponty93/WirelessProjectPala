package Utils;

import android.content.ClipData;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.project.wirelessteam.wirelessproject.BoardActivity;

public class onTouchCustomMethod implements View.OnTouchListener {

    private RelativeLayout refLayout = null;
    private Context boardContext = null;

    public onTouchCustomMethod(Context context, RelativeLayout lay) {
        refLayout = lay;
        boardContext = context;

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        ClipData dragData = ClipData.newPlainText("", (String) view.getTag());
        View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
        view.startDrag(dragData, shadow, view, 0);
        return true;

    }
}
