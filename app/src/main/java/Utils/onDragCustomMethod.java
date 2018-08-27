package Utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class onDragCustomMethod implements View.OnDragListener {
    private Context cont = null;

    public onDragCustomMethod(Context con) {
        cont = con;
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

                /*// Get dragged view object from drag event object.
                View srcView = (View)dragEvent.getLocalState();
                // Get dragged view's parent view group.
                ViewGroup owner = (ViewGroup) srcView.getParent();
                // Remove source view from original parent view group.
                owner.removeView(srcView);

                View newParent = (View) view;
                //newParent.addView(srcView);*/
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

    private void resetTargetViewBackground(View view)
    {
        // Clear color filter.
        view.getBackground().clearColorFilter();

        // Redraw the target view use original color.
        view.invalidate();
    }
}
