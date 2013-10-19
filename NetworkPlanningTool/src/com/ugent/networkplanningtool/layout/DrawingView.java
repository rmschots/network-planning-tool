package com.ugent.networkplanningtool.layout;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ugent.networkplanningtool.model.FloorModel;

public class DrawingView extends View implements Observer{

	private Paint paint = new Paint();
	
	private FloorModel model = null;

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(model != null){
			paint.setColor(Color.BLACK);
			
			int viewWidth = getWidth();
			int viewHeight = getHeight();
			
			float percentOffsetX = (FloorModel.INTERVAL-(model.getOffsetX() % FloorModel.INTERVAL))/FloorModel.INTERVAL%1;
			float percentOffsetY = (FloorModel.INTERVAL-(model.getOffsetY() % FloorModel.INTERVAL))/FloorModel.INTERVAL%1;
			
			for(float i = percentOffsetX*model.getPixelsPerInterval(); i < viewWidth; i+=model.getPixelsPerInterval()){
				canvas.drawLine(i, 0f, i, viewHeight, paint);
			}
			
			for(float i = percentOffsetY*model.getPixelsPerInterval(); i < viewWidth; i+=model.getPixelsPerInterval()){
				canvas.drawLine(0, i, viewWidth, i, paint);
			}
		}
		super.onDraw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			break;

        case MotionEvent.ACTION_MOVE:
        	if(model.isMoving()){
        		model.move(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
        	}
            break;
        case MotionEvent.ACTION_UP:
            break;
        case MotionEvent.ACTION_CANCEL:
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
        	model.moveStart(event.getX(0),event.getY(0),event.getX(1),event.getY(1));
            break;
        case MotionEvent.ACTION_POINTER_UP:
        	model.moveStop();
            break;
        default: break;
		}
		return true;
	}

	@Override
	public void update(Observable observable, Object data) {
		invalidate();
	}

	/**
	 * @return the model
	 */
	public FloorModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(FloorModel model) {
		this.model = model;
		model.addObserver(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if(model != null){
			model.setViewSize(w, h);
			invalidate();
		}
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	
	
}
