package com.ugent.networkplanningtool.layout;

import java.util.Observer;

import com.ugent.networkplanningtool.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ScaleImageView extends ImageView{
	
	
	private double maxZoomIn = 2.0;
	private double maxZoomOut = 0.25;
	private Paint paint = new Paint();
	
	private double scale = 1;
	
	private Point coord1 = null;
	private Point coord2 = null;
	
	private Bitmap bm;

	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.vl));
	}
	
	public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.vl));
	}
	
	public void setBitmap(Bitmap bm){
		this.bm = bm;
		setImageBitmap(bm);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(coord1 != null){
			paint.setColor(Color.BLACK);
			canvas.drawCircle(coord1.x, coord1.y, 10, paint);
			if(coord2 != null){
				paint.setStrokeWidth(3);
				canvas.drawCircle(coord2.x, coord2.y, 10, paint);
				canvas.drawLine(coord1.x, coord1.y, coord2.x, coord2.y, paint);
			}
		}
		
	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			if(coord2 == null && coord1 != null){
				coord2 = new Point((int)event.getX(), (int)event.getY());
			}else{
				coord1 = new Point((int)event.getX(), (int)event.getY());
				coord2 = null;
			}
			break;
		}
		invalidate();
		Log.d("DEBUG","coord: "+coord1);
		return true;
	}

	public Point getCoord1() {
		return coord1;
	}

	public Point getCoord2() {
		return coord2;
	}
	
	public void zoomIn(){
		double oldScale = scale;
		if(scale*2 < maxZoomIn){
			scale*=2;
		}else{
			scale = maxZoomIn;
		}
		try{
			Bitmap b = Bitmap.createScaledBitmap(bm, (int) (bm.getWidth()*scale), (int) (bm.getHeight()*scale), false);
			setImageBitmap(b);
		}catch(OutOfMemoryError ex){
			Log.d("DEBUG","ERROR");
			scale/=2;
		}
		
		if(oldScale != scale){
			double diff = scale/oldScale;
			coord1.x*=diff;
			coord1.y*=diff;
			coord2.x*=diff;
			coord2.y*=diff;
		}
	}
	
	public void zoomOut(){
		double oldScale = scale;
		if(scale/2 > maxZoomOut){
			scale/=2;
		}else{
			scale = maxZoomOut;
		}
		Bitmap b = Bitmap.createScaledBitmap(bm, (int) (bm.getWidth()*scale), (int) (bm.getHeight()*scale), false);
		setImageBitmap(b);
		if(oldScale != scale){
			double diff = scale/oldScale;
			coord1.x*=diff;
			coord1.y*=diff;
			coord2.x*=diff;
			coord2.y*=diff;
		}
	}
	
	public double getImageScale(){
		return Math.sqrt(Math.pow((coord1.x-coord2.x), 2) + Math.pow((coord1.y-coord2.y), 2))/scale;
	}
}
