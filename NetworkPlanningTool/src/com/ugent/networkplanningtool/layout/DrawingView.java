package com.ugent.networkplanningtool.layout;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ActivityType;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.model.FloorPlanModel;
import com.ugent.networkplanningtool.model.DrawingModel.PlaceResult;

public class DrawingView extends View implements Observer{

	private Paint paint = new Paint();
	
	private DrawingModel drawingModel = null;
	private FloorPlanModel floorPlanModel = FloorPlanModel.getInstance();

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		floorPlanModel.addObserver(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(drawingModel != null){
			drawGrid(canvas);
			drawWalls(canvas);
			drawActivities(canvas);
			drawConnectionPoints(canvas);
			drawAccessPoints(canvas);
			drawTouch(canvas);
		}
		super.onDraw(canvas);
	}
	
	private void drawAccessPoints(Canvas canvas) {
		List<AccessPoint> accessPointList = floorPlanModel.getAccessPointList();		
		for(AccessPoint ap : accessPointList){
			ap.drawOnCanvas(canvas, drawingModel, paint);
		}
	}

	private void drawConnectionPoints(Canvas canvas) {
		List<ConnectionPoint> connectionPointList = floorPlanModel.getConnectionPointList();		
		for(ConnectionPoint cp : connectionPointList){
			cp.drawOnCanvas(canvas, drawingModel, paint);
		}
		
	}

	private void drawActivities(Canvas canvas) {
		List<DataActivity> activityList = floorPlanModel.getDataActivityList();		
		for(DataActivity cp : activityList){
			cp.drawOnCanvas(canvas, drawingModel, paint);
		}
	}

	private void drawWalls(Canvas canvas) {
		List<Wall> wallList = floorPlanModel.getWallList();
		for(Wall w : wallList){
			w.drawOnCanvas(canvas, drawingModel, paint);
		}
	}
	
	private void drawTouch(Canvas canvas) {
		// TODO beter systeem
		DataObject tw = drawingModel.getTouchDataObject();
		if(tw != null){
			if(tw.isComplete()){
				tw.drawOnCanvas(canvas, drawingModel, paint);
			}else{
				if(tw instanceof Wall && ((Wall)tw).canDraw()){
					tw.drawOnCanvas(canvas, drawingModel, paint);
				}
			}
			
		}
	}

	private void drawGrid(Canvas canvas){
		paint.setColor(Color.BLACK);
		
		int viewWidth = getWidth();
		int viewHeight = getHeight();
		
		float percentOffsetX = (DrawingModel.INTERVAL-(drawingModel.getOffsetX() % DrawingModel.INTERVAL))/DrawingModel.INTERVAL%1;
		float percentOffsetY = (DrawingModel.INTERVAL-(drawingModel.getOffsetY() % DrawingModel.INTERVAL))/DrawingModel.INTERVAL%1;
		
		for(float i = percentOffsetX*drawingModel.getPixelsPerInterval(); i < viewWidth; i+=drawingModel.getPixelsPerInterval()){
			canvas.drawLine(i, 0f, i, viewHeight, paint);
		}
		
		for(float i = percentOffsetY*drawingModel.getPixelsPerInterval(); i < viewWidth; i+=drawingModel.getPixelsPerInterval()){
			canvas.drawLine(0, i, viewWidth, i, paint);
		}
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		event.setLocation(event.getX()-100,event.getY()-100);
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			drawingModel.setTouchLocation((int)event.getX(0), (int)event.getY(0));
			break;
        case MotionEvent.ACTION_MOVE:
        	if(drawingModel.isMoving()){
        		drawingModel.move((int)event.getX(0), (int)event.getY(0), (int)event.getX(1), (int)event.getY(1));
        	}else if(drawingModel.isPlacing()){
        		drawingModel.setTouchLocation((int)event.getX(0), (int)event.getY(0));
        	}
            break;
        case MotionEvent.ACTION_UP:
        	if(drawingModel.isPlacing()){
        		PlaceResult pr = drawingModel.place();
        		if(!pr.equals(PlaceResult.SUCCESS)){
        			Toast.makeText(getContext(), pr.getErrorMessage(), Toast.LENGTH_SHORT).show();
        		}
        	}
            break;
        case MotionEvent.ACTION_CANCEL:
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
        	drawingModel.moveStart((int)event.getX(0),(int)event.getY(0),(int)event.getX(1),(int)event.getY(1));
            break;
        case MotionEvent.ACTION_POINTER_UP:
        	drawingModel.moveStop();
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
		return drawingModel;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
		drawingModel.addObserver(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if(drawingModel != null){
			drawingModel.setViewSize(w, h);
			invalidate();
		}
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
}
