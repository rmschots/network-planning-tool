package com.ugent.networkplanningtool.layout;

import com.ugent.networkplanningtool.model.FloorModel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

	private Paint paint = new Paint();
	
	private FloorModel model;
	
	private double distanceStart = 0;
	private float dragStartX = 0;
	private float dragStartY = 0;

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDesign();
	}

	private void setupDesign() {
		model = new FloorModel();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			break;

        case MotionEvent.ACTION_MOVE:
        	if(distanceStart > 0){
        		double distanceMoved = getDistance(event);
        		
        		model.setPixelsPerInterval(model.getPixelsPerInterval()/(float)(distanceStart/distanceMoved));
        		distanceStart = distanceMoved;
        		
        		model.setOffsetX(dragStartX-Math.min(event.getX(0), event.getX(1))/model.getPixelsPerInterval()*FloorModel.INTERVAL);
        		model.setOffsetY(dragStartY-Math.min(event.getY(0), event.getY(1))/model.getPixelsPerInterval()*FloorModel.INTERVAL);
        		
        		invalidate();
        	}
            break;
        case MotionEvent.ACTION_UP:
        	
            //first finger went up
            break;
        case MotionEvent.ACTION_CANCEL:
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
        	distanceStart = getDistance(event);
        	dragStartX = model.getOffsetX()+Math.min(event.getX(0), event.getX(1))/model.getPixelsPerInterval()*FloorModel.INTERVAL;
        	Log.d("PEACOCK",""+dragStartX);
        	dragStartY = model.getOffsetY()+Math.min(event.getY(0), event.getY(1))/model.getPixelsPerInterval()*FloorModel.INTERVAL;
            break;
        case MotionEvent.ACTION_POINTER_UP:
        	distanceStart = 0;
            break;
        default: break;
		}
		return true;
	}

	private double getDistance(MotionEvent event) {
		double distance1 = Math.sqrt(Math.pow(event.getX(1)-event.getX(0), 2)+
				Math.pow(event.getY(1)-event.getY(0), 2));
		return distance1;
	}


}
