package com.ugent.networkplanningtool.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DesignView extends View{
	
	private Paint paint = new Paint();

	public DesignView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDesign();
	}

	private void setupDesign() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(Color.BLUE);
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight()/2, paint);
	}
	
	
}
