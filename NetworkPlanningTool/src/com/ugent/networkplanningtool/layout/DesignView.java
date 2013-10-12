package com.ugent.networkplanningtool.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DesignView extends View {

	private Paint paint = new Paint();
	
	private MotionEvent zoomStart;
	float lines = 10;

	public DesignView(Context context, AttributeSet attrs) {
		super(context, attrs);
		zoomStart = null;
		setupDesign();
	}

	private void setupDesign() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(Color.BLACK);
		int height = getHeight()-1;
		int width = getWidth()-1;
		for(int i = 0; i <= lines; i++){
			canvas.drawLine(0, height*i/lines, width, height*i/lines, paint);
		}
		for(int i = 0; i <= lines; i++){
			canvas.drawLine(width*i/lines, 0, width*i/lines, height, paint);
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("DEBUG",""+(event.getAction() & MotionEvent.ACTION_MASK));
		float touchX = event.getX();
		float touchY = event.getY();

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			Log.d("DEBUG","FINGER 1 DOWN");
			break;

        case MotionEvent.ACTION_MOVE:
        	if(zoomStart != null){
        		
        	}
        	Log.d("DEBUG","FINGER 1 MOVE");
            break;

        case MotionEvent.ACTION_UP:
        	Log.d("DEBUG","FINGER 1 UP");
            //first finger went up
            break;

        case MotionEvent.ACTION_CANCEL:
        	Log.d("DEBUG","FINGER 1 CANCEL");
            break;

        case MotionEvent.ACTION_POINTER_DOWN:
        	zoomStart = event;
        	Log.d("DEBUG","FINGER 2 DOWN");
            //second finger (or third, or more) went down.
            break;

        case MotionEvent.ACTION_POINTER_UP:
        	zoomStart = null;
            //second finger (or more) went up.
        	Log.d("DEBUG","FINGER 2 UP");
            break;

        default: break;
		}
		return true;
	}


}
