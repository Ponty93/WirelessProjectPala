package Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.project.wirelessteam.Views.R;

public class pawnView extends AppCompatImageView{
    private String pawnNumber;
    private Paint paintShapeText;

    public pawnView(Context context) {
        super(context);

    }

    public pawnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        setupPaint();
    }

    public pawnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        setupPaint();
    }


    private void init(AttributeSet attrs) {
        // Obtain a typed array of attributes
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.cellView, 0, 0);
        // Extract custom attributes into member variables
        try {
            pawnNumber = a.getString(R.styleable.pawnView_pawnNumber);

        } finally {
            // TypedArray objects are shared and must be recycled.
            a.recycle();
        }
    }

    public void setPawnText(String text){
        pawnNumber = text;
        invalidate();
        requestLayout();
    }
    public String getPawnText() {
        return pawnNumber;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintShapeText.setTextSize(42);
        if(pawnNumber != null)
            canvas.drawText(pawnNumber,36,59,paintShapeText);
    }



    private void setupPaint() {

        paintShapeText = new Paint();
        paintShapeText.setStyle(Paint.Style.FILL);
        paintShapeText.setColor(Color.WHITE);
        paintShapeText.setFakeBoldText(true);


    }


}
