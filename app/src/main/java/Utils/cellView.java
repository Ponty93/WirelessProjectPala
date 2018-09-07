package Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.project.wirelessteam.Views.R;

public class cellView extends View {
    private String cellText;
    private int cellColor;
    private int textWidth = 10;
    private int textHeight = 30;
    private Paint paintShapeText;
    private Paint paintShapeBorder;


    public cellView(Context context) {
        super(context);

    }

    public cellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        setupPaint();
    }

    public cellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        setupPaint();
    }

    public cellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
        setupPaint();
    }


    private void init(AttributeSet attrs) {
        // Obtain a typed array of attributes
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.cellView, 0, 0);
        // Extract custom attributes into member variables
        try {
            cellText = a.getString(R.styleable.cellView_cellText);
            cellColor = a.getColor(R.styleable.cellView_cellColor,Color.BLACK);
        } finally {
            // TypedArray objects are shared and must be recycled.
            a.recycle();
        }
    }


    public void setCellText(String text){
        cellText = text;
        invalidate();
        requestLayout();
    }

    public void setCellColor(int color){
        cellColor = color;
        invalidate();
        requestLayout();
    }

    public int getCellColor(){return cellColor;}
    public String getCellText() {
        return cellText;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintShapeText.setTextSize(30);
        canvas.drawText(cellText,textWidth,textHeight,paintShapeText);
        canvas.drawRect(0,0,getWidth(),getHeight(),paintShapeBorder);

    }



    private void setupPaint() {

        paintShapeText = new Paint();
        paintShapeText.setStyle(Paint.Style.FILL);
        paintShapeText.setColor(cellColor);
        paintShapeText.setTextSize(60);
        paintShapeText.setFakeBoldText(true);

        paintShapeBorder = new Paint();
        paintShapeBorder.setStyle(Paint.Style.STROKE);
        paintShapeBorder.setStrokeWidth(10);
        paintShapeBorder.setColor(cellColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){

        int contentWidth = textWidth;
        int width = resolveSizeAndState(50,widthMeasureSpec,0);
        int height = resolveSizeAndState(80,heightMeasureSpec,0);
        setMeasuredDimension(width,height);
    }
}

