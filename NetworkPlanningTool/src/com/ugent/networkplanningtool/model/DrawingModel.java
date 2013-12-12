package com.ugent.networkplanningtool.model;

import java.util.Observable;
import java.util.Stack;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;
import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.ConnectionPoint;
import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.Material;
import com.ugent.networkplanningtool.data.Thickness;
import com.ugent.networkplanningtool.data.Wall;
import com.ugent.networkplanningtool.data.WallType;
import com.ugent.networkplanningtool.utils.Utils;

public class DrawingModel extends Observable {
	
	public static enum PlaceResult{
		SUCCESS(null),
		CONNECTION_POINT_NOT_ADJACENT_TO_WALL(MainActivity.getContext().getResources().getString(R.string.connectionPointNotAdjacentToWall)),
		NOTHING_TO_PLACE(MainActivity.getContext().getResources().getString(R.string.nothingToPlace));
		private String errorMessage;
		private PlaceResult(String errorMessage){
			this.errorMessage = errorMessage;
		}
		public String getErrorMessage(){
			return errorMessage;
		}
	}

	// states the drawing area can be in
	private static enum STATE {
		IDLE, MOVING, PLACING
	}
	private STATE state;

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

	// dimensions of the actual view (in pixels)
	private int viewWidth;
	private int viewHeight;

	// Touch object info
	private DataObject touchDataObject = new Wall(WallType.WALL, Thickness.THIN, Material.BRICK);

	private boolean zoomInMaxed;
	private boolean zoomOutMaxed;

	private boolean snapToGrid = true;

	public DrawingModel(int viewWidth, int viewHeight) {
		offsetX = 0;
		offsetY = 0;

		state = STATE.IDLE;
		
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

	public int getActualLocationX(float x) {
		return (int) (offsetX + x / pixelsPerInterval * DrawingModel.INTERVAL);
	}

	public int getActualLocationY(float y) {
		return (int) (offsetY + y / pixelsPerInterval * DrawingModel.INTERVAL);
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

	public boolean isMoving() {
		return state.equals(STATE.MOVING);
	}

	public boolean isPlacing() {
		return state.equals(STATE.PLACING);
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
		touchDataObject = touchDataObject.getPartialDeepCopy();
		state = STATE.MOVING;
		distanceStart = calculateDistance(x1, y1, x2, y2);
		dragStart = new PointF(offsetX + Math.min(x1, x2) / pixelsPerInterval* DrawingModel.INTERVAL,
				offsetY + Math.min(y1, y2) / pixelsPerInterval * DrawingModel.INTERVAL);
		setChanged();
		notifyObservers();
	}

	public void moveStop() {
		state = STATE.IDLE;
		setChanged();
		notifyObservers();
	}

	public PlaceResult place() {
		Log.d("debug","place");
		state = STATE.IDLE;
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
						Wall closestWall = FloorPlanModel.getInstance().getClosestWallToPoint(touchDataObject.getPoint1());
						if(closestWall != null){
							float dist = Utils.pointToLineDistance(closestWall.getPoint1(), closestWall.getPoint2(),touchDataObject.getPoint1(),false);
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
			Point wallPoint = new Point(getActualLocationX(x),getActualLocationY(y));
			if(touchDataObject instanceof Wall){
				Point closestCorner = FloorPlanModel.getInstance().getClosestCornerToPoint(wallPoint);
				if(closestCorner != null && Utils.pointToPointDistance(wallPoint, closestCorner) <= INTERVAL/2){
					wallPoint = closestCorner;
				}else{
					if (snapToGrid) {
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
					}else{
						Wall closestWall = FloorPlanModel.getInstance().getClosestWallToPoint(wallPoint);
						if(closestWall != null && Utils.pointToLineDistance(closestWall.getPoint1(), closestWall.getPoint2(),wallPoint,false) <= INTERVAL/2){
							Point p = Utils.pointProjectionOnLine(closestWall.getPoint1(), closestWall.getPoint2(), wallPoint);
							if(p != null){
								wallPoint = p;
							}else{
								Log.e("DEBUG","closestWall NULL");
							}
						}
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

	public boolean isSnapToGrid() {
		return snapToGrid;
	}

	public void setSnapToGrid(boolean snapToGrid) {
		this.snapToGrid = snapToGrid;
	}
	
}
