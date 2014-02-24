package com.ugent.networkplanningtool.layout;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.R.drawable;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.DataActivity;
import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.DeusResult;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.layout.dataobject.AccessPointView;
import com.ugent.networkplanningtool.layout.dataobject.ConnectionPointView;
import com.ugent.networkplanningtool.layout.dataobject.DataActivityView;
import com.ugent.networkplanningtool.layout.dataobject.DataObjectView;
import com.ugent.networkplanningtool.layout.dataobject.DataObjectView.ViewType;
import com.ugent.networkplanningtool.layout.dataobject.WallView;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.model.DrawingModel.PlaceResult;
import com.ugent.networkplanningtool.model.FloorPlanModel;
import com.ugent.networkplanningtool.model.OptimizeResultModel;

public class DrawingView extends View implements Observer{

	private Paint paint = new Paint();
	
	private DrawingModel drawingModel = null;

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		FloorPlanModel.getInstance().addObserver(this);
		setDrawingCacheEnabled(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(drawingModel != null){
			drawBackground(canvas);
			drawGrid(canvas);
			drawResults(canvas);
			drawWalls(canvas);
			drawActivities(canvas);
			drawConnectionPoints(canvas);
			drawAccessPoints(canvas);
			drawTouch(canvas);
		}
		super.onDraw(canvas);
	}
	
	private void drawResults(Canvas canvas) {
		OptimizeResultModel orm = OptimizeResultModel.getInstance();
		if(!orm.getResultList().isEmpty()){
			for(DeusResult or : orm.getResultList()){
				or.drawOnCanvas(canvas, drawingModel, paint, false);
			}
		}
	}
	
	private void drawBackground(Canvas canvas) {
		if(drawingModel.getBackgroundImage() != null){
			Bitmap bgImg = drawingModel.getBackgroundImage();
			double scale = drawingModel.getBackgroundScale();
			Matrix m = new Matrix();
			double newScale = drawingModel.getPixelsPerInterval()/(scale*DrawingModel.INTERVAL*2);
			m.postScale((float)newScale, (float)newScale);
			float offsetX = drawingModel.getOffsetX();
			float offsetY = drawingModel.getOffsetY();
			
			float offsetXpx = offsetX*drawingModel.getPixelsPerInterval()/DrawingModel.INTERVAL;
			float offsetYpx = offsetY*drawingModel.getPixelsPerInterval()/DrawingModel.INTERVAL;
			m.postTranslate(-offsetXpx, -offsetYpx);
			canvas.drawBitmap(bgImg, m, paint);
		}
	}
	
	private void drawAccessPoints(Canvas canvas) {
		List<AccessPoint> accessPointList = FloorPlanModel.getInstance().getAccessPointList();		
		for(AccessPoint ap : accessPointList){
			ap.drawOnCanvas(canvas, drawingModel, paint, false);
		}
	}

	private void drawConnectionPoints(Canvas canvas) {
		List<ConnectionPoint> connectionPointList = FloorPlanModel.getInstance().getConnectionPointList();		
		for(ConnectionPoint cp : connectionPointList){
			cp.drawOnCanvas(canvas, drawingModel, paint, false);
		}
		
	}

	private void drawActivities(Canvas canvas) {
		List<DataActivity> activityList = FloorPlanModel.getInstance().getDataActivityList();		
		for(DataActivity cp : activityList){
			cp.drawOnCanvas(canvas, drawingModel, paint, false);
		}
	}

	private void drawWalls(Canvas canvas) {
		List<Wall> wallList = FloorPlanModel.getInstance().getWallList();
		for(Wall w : wallList){
			w.drawOnCanvas(canvas, drawingModel, paint, false);
		}
	}
	
	private void drawTouch(Canvas canvas) {
		DataObject tw = drawingModel.getTouchDataObject();
		if(tw != null && tw.canDraw()){
			tw.drawOnCanvas(canvas, drawingModel, paint, true);
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
		MotionEvent movedEvent = MotionEvent.obtain(event);
		movedEvent.setLocation(event.getX()-50,event.getY()-50);
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			Log.d("DEBUG","ACION: "+drawingModel.getState());
			switch(drawingModel.getState()){
			case PRE_SELECTING_EDIT:
			case PRE_SELECTING_INFO:
			case PRE_SELECTING_REMOVE:
				drawingModel.startSelect(movedEvent.getX(0), movedEvent.getY(0));
				break;
			default:
				drawingModel.setTouchLocation(movedEvent.getX(0), movedEvent.getY(0));
				break;
			
			}
			break;
        case MotionEvent.ACTION_MOVE:
        	if(drawingModel.isMoving()){
        		drawingModel.move(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
        	}else{
        		switch(drawingModel.getState()){
    			case PLACING:
    				drawingModel.setTouchLocation(movedEvent.getX(0), movedEvent.getY(0));
    				break;
    			case SELECTING_EDIT:
    			case SELECTING_INFO:
    			case SELECTING_REMOVE:
    				drawingModel.select(movedEvent.getX(0), movedEvent.getY(0));
    				break;
    			default:
    				break;
            	
            	}
        	}
            break;
        case MotionEvent.ACTION_UP:
        	switch(drawingModel.getState()){
			case PLACING:
				PlaceResult pr = drawingModel.place();
        		if(!pr.equals(PlaceResult.SUCCESS)){
        			Toast.makeText(getContext(), pr.getErrorMessage(), Toast.LENGTH_SHORT).show();
        		}
				break;
			case SELECTING_EDIT:
				DataObject dObj = drawingModel.getSelected();
        		if(dObj != null){
        			AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        			alert.setTitle("Edit");
        			DataObjectView view = null;
        			switch (dObj.DATA_OBJECT_TYPE) {
					case ACCESS_POINT:
						view = new AccessPointView(getContext(), ViewType.EDIT, drawingModel);
						break;
					case CONNECTION_POINT:
						view = new ConnectionPointView(getContext(), ViewType.EDIT, drawingModel);
						break;
					case DATA_ACTIVITY:
						view = new DataActivityView(getContext(), ViewType.EDIT, drawingModel);
						break;
					case WALL:
						view = new WallView(getContext(), ViewType.EDIT, drawingModel);
						break;
					default:
						break;
					}
        			alert.setView(view);
        			alert.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							drawingModel.deselect();
						}
					});
        			alert.setPositiveButton(android.R.string.ok, null);
        			AlertDialog d = alert.create();
        			d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        			d.show();
        		}else{
        			drawingModel.deselect();
        		}
				break;
			case SELECTING_INFO:
				dObj = drawingModel.getSelected();
        		if(dObj != null){
        			AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        			alert.setTitle("Info");
        			DataObjectView view = null;
        			switch (dObj.DATA_OBJECT_TYPE) {
					case ACCESS_POINT:
						view = new AccessPointView(getContext(), ViewType.INFO, drawingModel);
						break;
					case CONNECTION_POINT:
						view = new ConnectionPointView(getContext(), ViewType.INFO, drawingModel);
						break;
					case DATA_ACTIVITY:
						view = new DataActivityView(getContext(), ViewType.INFO, drawingModel);
						break;
					case WALL:
						view = new WallView(getContext(), ViewType.INFO, drawingModel);
						break;
					default:
						break;
					}
        			alert.setView(view);
        			alert.setPositiveButton(android.R.string.ok, null);
        			AlertDialog d = alert.create();
        			d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        			d.show();
        		}
        		drawingModel.deselect();
				break;
			case SELECTING_REMOVE:
				dObj = drawingModel.getSelected();
        		if(dObj != null){
        			FloorPlanModel.getInstance().removeDataObject(dObj);
        		}
        		drawingModel.deselect();
				break;
			default:
				break;
        	
        	}
            break;
        case MotionEvent.ACTION_CANCEL:
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
        	drawingModel.moveStart(event.getX(0),event.getY(0),event.getX(1),event.getY(1));
            break;
        case MotionEvent.ACTION_POINTER_UP:
        	if(drawingModel.isMoving()){
        		drawingModel.moveStop();
        		switch(drawingModel.getState()){
				case PLACING:
					drawingModel.setPlaceMode();
					break;
				default:
					break;
        		}
        		
        	}
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
