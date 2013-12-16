package com.ugent.networkplanningtool.model;

import java.util.Observable;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.Material;
import com.ugent.networkplanningtool.data.SnapTo;
import com.ugent.networkplanningtool.data.Thickness;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.WallType;
import com.ugent.networkplanningtool.utils.Couple;
import com.ugent.networkplanningtool.utils.Utils;

public class DrawingModel extends Observable {
	
	public static enum PlaceResult{
		SUCCESS(null),
		CONNECTION_POINT_NOT_ADJACENT_TO_WALL(MainActivity.getInstance().getResources().getString(R.string.connectionPointNotAdjacentToWall)),
		NOTHING_TO_PLACE(MainActivity.getInstance().getResources().getString(R.string.nothingToPlace));
		private String errorMessage;
		private PlaceResult(String errorMessage){
			this.errorMessage = errorMessage;
		}
		public String getErrorMessage(){
			return errorMessage;
		}
	}

	// states the drawing area can be in
	public static enum STATE {
		IDLE, PRE_PLACE, PLACING, SELECTING_INFO, PRE_SELECTING_INFO, SELECTING_EDIT, PRE_SELECTING_EDIT, SELECTING_REMOVE, PRE_SELECTING_REMOVE
	}
	private STATE state;
	
	private boolean moving;

	// ground plan dimensions (in units)
	public static final int INTERVAL = 50;
	public static int FLOOR_WIDTH = 4750;
	public static int FLOOR_HEIGHT = 3550;

	// maximum allowed zoom (in units)
	private static final int maxZoomIn = 200;

	// location on ground plan to view
	private float offsetX; // in units
	private float offsetY; // in units
	private float pixelsPerInterval; // in pixels
	private static final float DEFAULT_PIXELS_PER_INTERVAL = 100;

	private double distanceStart = -1; // in pixels
	private PointF dragStart = null; // in units
	private Point touchLocation = null;

	// dimensions of the actual view (in pixels)
	private int viewWidth;
	private int viewHeight;

	// Touch object info
	private DataObject touchDataObject = new Wall(WallType.WALL, Thickness.THIN, Material.BRICK);

	private boolean zoomInMaxed;
	private boolean zoomOutMaxed;

	private SnapTo snapTo = SnapTo.GRID;

	public DrawingModel(int viewWidth, int viewHeight) {
		offsetX = 0;
		offsetY = 0;

		state = STATE.IDLE;
		moving = false;
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;

		if (viewWidth == 0 || viewHeight == 0) {
			this.pixelsPerInterval = DEFAULT_PIXELS_PER_INTERVAL;
		} else {
			setPixelsPerInterval(DEFAULT_PIXELS_PER_INTERVAL);
		}
	}

	private double calculateDistance(float x1, float y1, float x2, float y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	public Point getTouchLocation(){
		return touchLocation;
	}
	
	public Point getActualTouchLocation(float x, float y){
		return new Point((int) (offsetX + x / pixelsPerInterval * DrawingModel.INTERVAL),
				(int) (offsetY + y / pixelsPerInterval * DrawingModel.INTERVAL));
	}

	public float getActualViewHeight() {
		return (viewHeight - 1) * INTERVAL / pixelsPerInterval;
	}

	public float getActualViewWidth() {
		return (viewWidth - 1) * INTERVAL / pixelsPerInterval;
	}

	public float getOffsetX() {
		return offsetX;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public float getPixelsPerInterval() {
		return pixelsPerInterval;
	}

	public boolean isZoomInMaxed() {
		return zoomInMaxed;
	}

	public boolean isZoomOutMaxed() {
		return zoomOutMaxed;
	}

	public void move(float x1, float y1, float x2, float y2) {
		double distanceMoved = calculateDistance(x1, y1, x2, y2);

		setPixelsPerInterval(pixelsPerInterval
				/ (float) (distanceStart / distanceMoved));
		distanceStart = distanceMoved;

		setOffsetX(dragStart.x - Math.min(x1, x2) / pixelsPerInterval
				* DrawingModel.INTERVAL);
		setOffsetY(dragStart.y - Math.min(y1, y2) / pixelsPerInterval
				* DrawingModel.INTERVAL);
		setChanged();
		notifyObservers();
	}

	public void moveStart(float x1, float y1, float x2, float y2) {
		switch(state){
		case PLACING:
			touchDataObject = touchDataObject.getPartialDeepCopy();
			break;
		case SELECTING_EDIT:
		case SELECTING_INFO:
		case SELECTING_REMOVE:
			deselect();
			break;
		default:
			break;
		
		}
		moving = true;
		distanceStart = calculateDistance(x1, y1, x2, y2);
		dragStart = new PointF(offsetX + Math.min(x1, x2) / pixelsPerInterval* DrawingModel.INTERVAL,
				offsetY + Math.min(y1, y2) / pixelsPerInterval * DrawingModel.INTERVAL);
		setChanged();
		notifyObservers();
	}

	public void moveStop() {
		moving = false;
		setChanged();
		notifyObservers();
	}

	public PlaceResult place() {
		Log.d("debug","place");
		state = STATE.PRE_PLACE;
		if(touchDataObject != null){
			if(touchDataObject instanceof Wall){
				Wall wall = (Wall) touchDataObject;
				if(wall.isComplete()){
					if(!(wall.getPoint1().x == wall.getPoint2().x && wall.getPoint1().y == wall.getPoint2().y)){
						FloorPlanModel.getInstance().addDataObject(wall);
					}
					touchDataObject = touchDataObject.getPartialDeepCopy();
				}else{
					wall.setPoint2(new Point(wall.getPoint1()));
				}
			}else{
				if(touchDataObject.isComplete()){
					if(touchDataObject instanceof ConnectionPoint){
						Couple<Double,Wall> closestWallCouple = FloorPlanModel.getInstance().getClosestWallToPoint(touchDataObject.getPoint1(),true);
						if(closestWallCouple != null){
							double dist = closestWallCouple.getA();
							if(dist <= INTERVAL/4 && dist != 0){
								FloorPlanModel.getInstance().addDataObject(touchDataObject);
								touchDataObject = touchDataObject.getPartialDeepCopy();
								setChanged();
								notifyObservers();
								return PlaceResult.SUCCESS;
							}
						}
						touchDataObject = touchDataObject.getPartialDeepCopy();
						setChanged();
						notifyObservers();
						return PlaceResult.CONNECTION_POINT_NOT_ADJACENT_TO_WALL;
					}
					FloorPlanModel.getInstance().addDataObject(touchDataObject);
					touchDataObject = touchDataObject.getPartialDeepCopy();
				}else{
					// should not happen (trying to place non-complete non-wall)
					Log.e("DEBUG","error: trying to place non-complete non-wall");
					touchDataObject = touchDataObject.getPartialDeepCopy();
				}
				
			}
			setChanged();
			notifyObservers();
			return PlaceResult.SUCCESS;
		}
		// else nothing to place
		return PlaceResult.NOTHING_TO_PLACE;
	}

	public void setOffsetX(float offsetX) {
		if (offsetX > 0) {
			float actualViewWidth = getActualViewWidth();
			if (offsetX + actualViewWidth < FLOOR_WIDTH) {
				this.offsetX = offsetX;
			} else {
				this.offsetX = FLOOR_WIDTH - actualViewWidth;
			}
		} else {
			this.offsetX = 0;
		}
		setChanged();
		notifyObservers();
	}

	public void setOffsetY(float offsetY) {
		if (offsetY > 0) {
			float actualViewHeight = getActualViewHeight();
			if (offsetY + actualViewHeight < FLOOR_HEIGHT) {
				this.offsetY = offsetY;
			} else {
				this.offsetY = FLOOR_HEIGHT - actualViewHeight;
			}
		} else {
			this.offsetY = 0;
		}
		setChanged();
		notifyObservers();
	}

	public void setPixelsPerInterval(float pixelsPerInterval) {
		if (pixelsPerInterval > 0) {
			if (pixelsPerInterval < Math.min(viewWidth - 1, viewHeight - 1)
					* INTERVAL / (float) maxZoomIn) {
				zoomInMaxed = false;
				float maxX = (viewWidth - 1) * INTERVAL / (float) FLOOR_WIDTH;
				float maxY = (viewHeight - 1) * INTERVAL / (float) FLOOR_HEIGHT;
				float max = Math.max(maxX, maxY);
				if (max < pixelsPerInterval) {
					zoomOutMaxed = false;
					this.pixelsPerInterval = pixelsPerInterval;
				} else {
					zoomOutMaxed = true;
					this.pixelsPerInterval = max;
					setOffsetX(getOffsetX());
					setOffsetY(getOffsetY());
				}

			} else {
				zoomInMaxed = true;
				zoomOutMaxed = false;
				this.pixelsPerInterval = Math
						.min(viewWidth - 1, viewHeight - 1)
						* INTERVAL
						/ (float) maxZoomIn;
			}
		}
	}

	public void setTouchLocation(float x, float y) {
		if(touchDataObject != null){
			state = STATE.PLACING;
			Point wallPoint = getActualTouchLocation(x,y);
			if(touchDataObject instanceof Wall){
				Point closestCorner = FloorPlanModel.getInstance().getClosestCornerToPoint(wallPoint);
				if(closestCorner != null && Utils.pointToPointDistance(wallPoint, closestCorner) <= INTERVAL/2){
					wallPoint = closestCorner;
				}else{
					switch (snapTo) {
					case GRID:
						int rest = wallPoint.x % INTERVAL;
						if (rest < INTERVAL / 2) {
							wallPoint.x = wallPoint.x - rest;
						} else {
							wallPoint.x = wallPoint.x + INTERVAL - rest;
						}
						rest = wallPoint.y % INTERVAL;
						if (rest < INTERVAL / 2) {
							wallPoint.y = wallPoint.y - rest;
						} else {
							wallPoint.y = wallPoint.y + INTERVAL - rest;
						}
						break;
					case WALLS:
						Couple <Double,Wall> closestWallCouple = FloorPlanModel.getInstance().getClosestWallToPoint(wallPoint,false);
						if(closestWallCouple != null && closestWallCouple.getA() <= INTERVAL/2){
							Wall closestWall = closestWallCouple.getB();
							Point p = Utils.pointProjectionOnLine(closestWall.getPoint1(), closestWall.getPoint2(), wallPoint);
							if(p != null){
								wallPoint = p;
							}else{
								Log.e("DEBUG","closestWall NULL");
							}
						}
					default:
						break;
					}
				}
				if(((Wall)touchDataObject).isComplete()){
					Wall wall = (Wall) touchDataObject;
					wall.setPoint2(wallPoint);
				}else{
					touchDataObject.setPoint1(wallPoint);
				}
			}else{
				touchDataObject.setPoint1(wallPoint);
			}
			touchLocation = wallPoint;
			setChanged();
			notifyObservers();
		}
	}

	public void setViewSize(int width, int height) {
		this.viewWidth = width;
		this.viewHeight = height;
		setPixelsPerInterval(this.pixelsPerInterval);
		setChanged();
		notifyObservers();
	}

	public void zoomIn() {
		setPixelsPerInterval(pixelsPerInterval * 2f);
		setChanged();
		notifyObservers();
	}

	public void zoomOut() {
		setPixelsPerInterval(pixelsPerInterval * 0.5f);
		setChanged();
		notifyObservers();
	}

	/**
	 * @return the touchWall
	 */
	public DataObject getTouchDataObject() {
		return touchDataObject;
	}

	public void setTouchDataObject(DataObject drawItem) {
		if(touchDataObject == drawItem){
			touchDataObject = touchDataObject.getPartialDeepCopy();
		}else{
			touchDataObject = drawItem;
		}
		setChanged();
		notifyObservers();
	}

	public SnapTo isSnapToGrid() {
		return snapTo;
	}

	public void setSnapToGrid(SnapTo snapToGrid) {
		this.snapTo = snapToGrid;
	}

	public void setInfoSelectionMode() {
		Log.d("DEBUG","INFOSELECTION MODE");
		state = STATE.PRE_SELECTING_INFO;
		setChanged();
		notifyObservers();
	}
	
	public void setEditSelectionMode(){
		Log.d("DEBUG","INFOEDIT MODE");
		state = STATE.PRE_SELECTING_EDIT;
		setChanged();
		notifyObservers();
	}
	
	public void setRemoveSelectionMode(){
		Log.d("DEBUG","INFOREMOVE MODE");
		state = STATE.PRE_SELECTING_REMOVE;
		setChanged();
		notifyObservers();
	}
	
	public void setPlaceMode(){
		state = STATE.PRE_PLACE;
		setChanged();
		notifyObservers();
	}
	
	public void setPlaceMode(DataObject dataObject){
		state = STATE.PRE_PLACE;
		setTouchDataObject(dataObject);
		setChanged();
		notifyObservers();
	}

	public void startSelect(float x, float y){
		switch(state){
		case PRE_SELECTING_EDIT:
			state = STATE.SELECTING_EDIT;
			break;
		case PRE_SELECTING_INFO:
			state = STATE.SELECTING_INFO;
			break;
		case PRE_SELECTING_REMOVE:
			state = STATE.SELECTING_REMOVE;
			break;
		default:
			break;
		}
		select(x,y);
	}
	
	public void select(float x, float y) {
		touchLocation = getActualTouchLocation(x,y);
		// get closest
		Couple<Double,DataObject> closestCouple = FloorPlanModel.getInstance().getClosestDataObjectToPoint(touchLocation);
		if(closestCouple==null){
			touchDataObject = null;
			return;
		}
		double distance = closestCouple.getA();
		if(distance < 40){
			touchDataObject = closestCouple.getB();
			touchLocation = touchDataObject.getPoint1();
		}else{
			touchDataObject = null;
		}
		setChanged();
		notifyObservers();
	}
	
	public DataObject getSelected(){
		switch(state){
		case SELECTING_EDIT:
		case SELECTING_INFO:
		case SELECTING_REMOVE:
			return touchDataObject;
		default:
			return null;
		
		}
	}
	
	public void deselect(){
		switch(state){
		case SELECTING_EDIT:
			state = STATE.PRE_SELECTING_EDIT;
			touchDataObject = null;
			setChanged();
			notifyObservers();
			break;
		case SELECTING_INFO:
			state = STATE.PRE_SELECTING_INFO;
			touchDataObject = null;
			setChanged();
			notifyObservers();
			break;
		case SELECTING_REMOVE:
			state = STATE.PRE_SELECTING_REMOVE;
			touchDataObject = null;
			setChanged();
			notifyObservers();
			break;
		default:
			break;
		}
	}
	
	public void setIdle(){
		state = STATE.IDLE;
		setChanged();
		notifyObservers();
	}
	
	public STATE getState(){
		return state;
	}
	
	public boolean isMoving(){
		return moving;
	}
}
