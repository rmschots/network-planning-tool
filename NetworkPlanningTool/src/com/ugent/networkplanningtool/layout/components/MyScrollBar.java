package com.ugent.networkplanningtool.layout.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ugent.networkplanningtool.model.DrawingModel;

import java.util.Observable;
import java.util.Observer;

/**
 * Custom implementation of a scroll bar both horizontally and vertically usable
 */
public class MyScrollBar extends View implements Observer {

    private Paint paint = new Paint();

    private RectF rect = new RectF();

    private boolean horizontal;
    private DrawingModel model;

    private int progressColor = Color.argb(50, 0, 0, 0);
    private int sliderColor = Color.argb(100, 0, 0, 0);

    private static final int progressBarHeight = 2;

    private float startDrag = -1;
    private float barStart;
    private float barEnd;


    /**
     * Default constructor
     * @param context the context of the parent
     */
    public MyScrollBar(Context context) {
        super(context);
        horizontal = getWidth() >= getHeight();
    }

    /**
     * Default constructor
     * @param context the context of the parent
     * @param attrs the attribute set
     */
    public MyScrollBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @return the model
     */
    public DrawingModel getModel() {
        return model;
    }

    /**
     * Sets the drawing model to use
     * @param model the drawing model to use
     */
    public void setModel(DrawingModel model) {
        this.model = model;
        model.addObserver(this);
    }

    /**
     * Draws te scroll bar on the canvas
     * @param canvas the canvas to draw on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (model != null) {
            if (horizontal) {
                float max = DrawingModel.FLOOR_WIDTH;
                float start = model.getOffsetX();
                float stop = start + model.getActualViewWidth();
                if (stop - start < max) {
                    int progheight = getPx(progressBarHeight);
                    paint.setColor(progressColor);
                    canvas.drawRect(0, (getHeight() - progheight) / 2, getWidth() - 1, (getHeight() + progheight) / 2, paint);
                    paint.setColor(sliderColor);
                    barStart = getWidth() * start / max;
                    barEnd = getWidth() * stop / max;
                    rect.set(barStart, 0, barEnd, getHeight() - 1);
                    canvas.drawRoundRect(rect, 20, 20, paint);
                }
            } else {
                float max = DrawingModel.FLOOR_HEIGHT;
                float start = model.getOffsetY();
                float stop = start + model.getActualViewHeight();
                if (stop - start < max) {
                    int progWidth = getPx(progressBarHeight);
                    paint.setColor(progressColor);
                    canvas.drawRect((getWidth() - progWidth) / 2, 0, (getWidth() + progWidth) / 2, getHeight() - 1, paint);
                    paint.setColor(sliderColor);
                    barStart = getHeight() * start / max;
                    barEnd = getHeight() * stop / max;
                    rect.set(0, getHeight() * start / max, getWidth() - 1, getHeight() * stop / max);
                    canvas.drawRoundRect(rect, 20, 20, paint);
                }
            }
        }
        super.onDraw(canvas);
    }

    /**
     * The user interaction with the scroll bar
     * @param event the touch event
     * @return wether to consume the event
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDrag = horizontal ? event.getX() : event.getY();
                if (startDrag < barStart || startDrag > barEnd) {
                    startDrag = -1;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (startDrag != -1) {
                    if (horizontal) {
                        barStart = barStart - startDrag + event.getX();
                        float newActualX = barStart * DrawingModel.FLOOR_WIDTH / getWidth();
                        model.setOffsetX(newActualX);
                        startDrag = event.getX();
                    } else {
                        barStart = barStart - startDrag + event.getY();
                        float newActualY = barStart * DrawingModel.FLOOR_HEIGHT / getHeight();
                        model.setOffsetY(newActualY);
                        startDrag = event.getY();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                startDrag = -1;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                model.moveStart(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                model.moveStop();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Updates the scroll bar when the drawing area dimensions change
     * @param w the new width
     * @param h the new height
     * @param oldw the old width
     * @param oldh the old height
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        horizontal = w >= h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * Invoked by a model when data changed
     * @param observable the model triggering this method
     * @param data the changed data
     */
    @Override
    public void update(Observable observable, Object data) {
        invalidate();
    }

    /**
     * Converts dp to px
     * @param dp the dp to convert
     * @return the resulting px
     */
    private int getPx(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }
}
