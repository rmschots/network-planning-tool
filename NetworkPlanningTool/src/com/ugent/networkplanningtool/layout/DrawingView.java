package com.ugent.networkplanningtool.layout;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.model.DrawingModel;

public class DrawingView extends View implements Observer{

	private Paint paint = new Paint();
	
	private DrawingModel model = null;

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(model != null){
			drawGrid(canvas);
			drawWalls(canvas);
			drawTouch(canvas);
		}
		super.onDraw(canvas);
	}
	
	private void drawWalls(Canvas canvas) {
		paint.setColor(Color.BLUE);
		int viewWidth = getWidth();
		int viewHeight = getHeight();
		List<Wall> wallList = model.getWallList();
		float circleRadius = model.getPixelsPerInterval()/4;
		for(Wall w : wallList){
			float pixelsX = (w.getWallX1()-model.getOffsetX())*model.getPixelsPerInterval()/DrawingModel.INTERVAL;
			float pixelsY = (w.getWallY1()-model.getOffsetY())*model.getPixelsPerInterval()/DrawingModel.INTERVAL;
			if(pixelsX > -circleRadius && pixelsY > -circleRadius && pixelsX < viewWidth+circleRadius && pixelsY < viewHeight+circleRadius){
				canvas.drawCircle(pixelsX, pixelsY, circleRadius, paint);
			}
		}
	}
	
	private void drawTouch(Canvas canvas) {
		if(model.isPlacing()){
			paint.setColor(Color.RED);
			float pixelsX = (model.getTouchLocationX()-model.getOffsetX())*model.getPixelsPerInterval()/DrawingModel.INTERVAL;
			float pixelsY = (model.getTouchLocationY()-model.getOffsetY())*model.getPixelsPerInterval()/DrawingModel.INTERVAL;
			canvas.drawCircle(pixelsX, pixelsY, model.getPixelsPerInterval()/4, paint);
		}
	}

	private void drawGrid(Canvas canvas){
		paint.setColor(Color.BLACK);
		
		int viewWidth = getWidth();
		int viewHeight = getHeight();
		
		float percentOffsetX = (DrawingModel.INTERVAL-(model.getOffsetX() % DrawingModel.INTERVAL))/DrawingModel.INTERVAL%1;
		float percentOffsetY = (DrawingModel.INTERVAL-(model.getOffsetY() % DrawingModel.INTERVAL))/DrawingModel.INTERVAL%1;
		
		for(float i = percentOffsetX*model.getPixelsPerInterval(); i < viewWidth; i+=model.getPixelsPerInterval()){
			canvas.drawLine(i, 0f, i, viewHeight, paint);
		}
		
		for(float i = percentOffsetY*model.getPixelsPerInterval(); i < viewWidth; i+=model.getPixelsPerInterval()){
			canvas.drawLine(0, i, viewWidth, i, paint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			model.setTouchLocation((int)event.getX(0), (int)event.getY(0));
			break;
        case MotionEvent.ACTION_MOVE:
        	if(model.isMoving()){
        		model.move((int)event.getX(0), (int)event.getY(0), (int)event.getX(1), (int)event.getY(1));
        	}else if(model.isPlacing()){
        		model.setTouchLocation((int)event.getX(0), (int)event.getY(0));
        	}
            break;
        case MotionEvent.ACTION_UP:
        	if(model.isPlacing()){
        		model.place();
        	}
            break;
        case MotionEvent.ACTION_CANCEL:
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
        	model.moveStart((int)event.getX(0),(int)event.getY(0),(int)event.getX(1),(int)event.getY(1));
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
	public DrawingModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(DrawingModel model) {
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
