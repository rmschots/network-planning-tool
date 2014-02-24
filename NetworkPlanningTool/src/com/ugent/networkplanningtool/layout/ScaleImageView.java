package com.ugent.networkplanningtool.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ScaleImageView extends ImageView{
	
	public static enum Mode{
		PRE_MOVE,
		DRAG,
		ZOOM,
		SELECT
	}
	
	private Paint paint = new Paint();
	
	private Point coord1 = null;
	private Point coord2 = null;
	
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF startPoint = new PointF();
	private PointF midPoint = new PointF();
	private float oldDist = 1f;
	private Mode mode = Mode.PRE_MOVE;
	
	float[] values = new float[9];

	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setBitmap(Bitmap bm){
		setImageBitmap(bm);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(coord1 != null){
			matrix.getValues(values);
			int x1 = (int) (coord1.x*values[0]+values[2]);
			int y1 = (int) (coord1.y*values[4]+values[5]);
			paint.setColor(Color.BLACK);
			canvas.drawCircle(x1, y1, 10, paint);
			if(coord2 != null){
				int x2 = (int) (coord2.x*values[0]+values[2]);
				int y2 = (int) (coord2.y*values[4]+values[5]);
				paint.setStrokeWidth(3);
				canvas.drawCircle(x2, y2, 10, paint);
				canvas.drawLine(x1, y1, x2, y2, paint);
			}
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mode == Mode.SELECT){
			matrix.getValues(values);
			float relativeX = (event.getX() - values[2]) / values[0];
			float relativeY = (event.getY() - values[5]) / values[4];
			switch (event.getAction() & MotionEvent.ACTION_MASK){
			case MotionEvent.ACTION_DOWN:
				if(coord2 == null && coord1 != null){
					coord2 = new Point((int)relativeX, (int)relativeY);
				}else{
					coord1 = new Point((int)relativeX, (int)relativeY);
					coord2 = null;
				}
				break;
			}
			invalidate();
		}else{
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix);
				startPoint.set(event.getX(), event.getY());
				mode = Mode.DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(midPoint, event);
					mode = Mode.ZOOM;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = Mode.PRE_MOVE;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == Mode.DRAG) {
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
				} else if (mode == Mode.ZOOM) {
					float newDist = spacing(event);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
					}
				}
				break;
			}
			setImageMatrix(matrix);
		}
		return true;
	}
	
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}
	
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	public Point getCoord1() {
		return coord1;
	}

	public Point getCoord2() {
		return coord2;
	}
	
	public void zoomIn(){
		matrix.postScale(2, 2, getWidth()/2, getHeight()/2);
		setImageMatrix(matrix);
	}
	
	public void zoomOut(){
		matrix.postScale(0.5f, 0.5f, getWidth()/2, getHeight()/2);
		setImageMatrix(matrix);
	}
	
	public void setMode(Mode mode){
		this.mode = mode;
	}
}
