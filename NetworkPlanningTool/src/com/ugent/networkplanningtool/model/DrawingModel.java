package com.ugent.networkplanningtool.model;

import java.util.Observable;

import android.util.Log;

import com.ugent.networkplanningtool.data.Material;
import com.ugent.networkplanningtool.data.Wall;

public class DrawingModel extends Observable {

	// states the drawing area can be in
	private static enum STATE {
		IDLE, MOVING, PLACING
	}
	private STATE state;

	// ground plan dimensions (in units)
	public static final int INTERVAL = 50;
	public static final int FLOOR_WIDTH = 3000;
	public static final int FLOOR_HEIGHT = 2000;

	// maximum allowed zoom (in units)
	private static final int maxZoomIn = 200;

	// location on ground plan to view
	private float offsetX; // in units
	private float offsetY; // in units
	private float pixelsPerInterval; // in pixels
	private static final float DEFAULT_PIXELS_PER_INTERVAL = 100;

	private double distanceStart = -1; // in pixels
	private float dragStartX = -1; // in units
	private float dragStartY = -1; // in units

	// dimensions of the actual view (in pixels)
	private int viewWidth;
	private int viewHeight;

	// Location on screen which is touched (in units)
	private Wall touchWall = null;
	private int touchLocationX = -1;
	private int touchLocationY = -1;

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

	public float getTouchLocationX() {
		return touchLocationX;
	}

	public float getTouchLocationY() {
		return touchLocationY;
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

		setOffsetX(dragStartX - Math.min(x1, x2) / pixelsPerInterval
				* DrawingModel.INTERVAL);
		setOffsetY(dragStartY - Math.min(y1, y2) / pixelsPerInterval
				* DrawingModel.INTERVAL);
		setChanged();
		notifyObservers();
	}

	public void moveStart(float x1, float y1, float x2, float y2) {
		state = STATE.MOVING;
		distanceStart = calculateDistance(x1, y1, x2, y2);
		dragStartX = offsetX + Math.min(x1, x2) / pixelsPerInterval
				* DrawingModel.INTERVAL;
		dragStartY = offsetY + Math.min(y1, y2) / pixelsPerInterval
				* DrawingModel.INTERVAL;
		setChanged();
		notifyObservers();
	}

	public void moveStop() {
		state = STATE.IDLE;
		setChanged();
		notifyObservers();
	}

	public void place() {
		if(touchLocationX != -1 && touchLocationY != -1){
			if(touchWall == null){
				touchWall = new Wall(touchLocationX, touchLocationY, Material.BRICK);
			}else{
				touchWall.setX2(touchLocationX);
				touchWall.setY2(touchLocationY);
				FloorPlanModel.getInstance().addWall(touchWall);
				touchWall = null;
			}
			touchLocationX = -1;
			touchLocationY = -1;
			state = STATE.IDLE;
			setChanged();
			notifyObservers();
		}else{
			Log.e("DEBUG","Error: Trying to create a non-existing wall");
		}
		
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
		state = STATE.PLACING;
		int wallX = getActualLocationX(x);
		int wallY = getActualLocationY(y);
		if (snapToGrid) {
			int rest = wallX % INTERVAL;
			if (rest < INTERVAL / 2) {
				wallX = wallX - rest;
			} else {
				wallX = wallX + INTERVAL - rest;
			}
			rest = wallY % INTERVAL;
			if (rest < INTERVAL / 2) {
				wallY = wallY - rest;
			} else {
				wallY = wallY + INTERVAL - rest;
			}
		}
		touchLocationX = wallX;
		touchLocationY = wallY;
		setChanged();
		notifyObservers();
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
	public Wall getTouchWall() {
		return touchWall;
	}
	
	
}
