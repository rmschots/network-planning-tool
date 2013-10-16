package com.ugent.networkplanningtool.model;

import java.util.Observable;


public class FloorModel extends Observable{
	
	private static enum STATE{
		IDLE,
		MOVING
	}
	
	private STATE state;
	
	public static final float INTERVAL = 0.5f;
	public static final float FLOOR_WIDTH = 100f;
	public static final float FLOOR_HEIGHT = 75f;
	
	private static final float maxZoomIn = 2f;
	
	private float offsetX;
	private float offsetY;
	private float pixelsPerInterval;
	
	private double distanceStart = 0;
	private float dragStartX = 0;
	private float dragStartY = 0;
	
	private int viewWidth;
	private int viewHeight;
	
	
	public FloorModel(int viewWidth, int viewHeight){
		offsetX = 0;
		offsetY = 0;
		pixelsPerInterval = 100;
		
		state = STATE.IDLE;
		
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
	}


	public float getOffsetX() {
		return offsetX;
	}
	
	public float getActualViewWidth(){
		return (viewWidth-1)*INTERVAL/pixelsPerInterval;
	}
	
	public float getActualViewHeight(){
		return (viewHeight-1)*INTERVAL/pixelsPerInterval;
	}


	public void setOffsetX(float offsetX) {
		if(offsetX > 0){
			float actualViewWidth = getActualViewWidth();
			if(offsetX + actualViewWidth < FLOOR_WIDTH){
				this.offsetX = offsetX;
			}else{
				this.offsetX = FLOOR_WIDTH - actualViewWidth;
			}
		}else{
			this.offsetX = 0;
		}
		setChanged();
		notifyObservers();
	}


	public float getOffsetY() {
		return offsetY;
	}


	public void setOffsetY(float offsetY) {
		if(offsetY > 0){
			float actualViewHeight = (viewHeight-1)*INTERVAL/pixelsPerInterval;
			if(offsetY + actualViewHeight < FLOOR_HEIGHT){
				this.offsetY = offsetY;
			}else{
				this.offsetY = FLOOR_HEIGHT - actualViewHeight;
			}
		}else{
			this.offsetY = 0;
		}
		setChanged();
		notifyObservers();
	}


	public float getPixelsPerInterval() {
		return pixelsPerInterval;
	}


	public void setPixelsPerInterval(float pixelsPerInterval) {
		if(pixelsPerInterval > 0){
			if(pixelsPerInterval*maxZoomIn/INTERVAL < Math.min(viewWidth, viewHeight)){
				
				float maxX = (viewWidth-1)*INTERVAL/FLOOR_WIDTH;
				float maxY = (viewHeight-1)*INTERVAL/FLOOR_WIDTH;
				float max = Math.max(maxX, maxY);
				if(max < pixelsPerInterval){
					this.pixelsPerInterval = pixelsPerInterval;
				}else{
					this.pixelsPerInterval = max;
				}
				
			}else{
				this.pixelsPerInterval = Math.min(viewWidth, viewHeight)*INTERVAL/maxZoomIn;
			}
		}
	}
	
	
	private double calculateDistance(float x1, float y1, float x2, float y2) {
		return Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
	}


	public void moveStart(float x1, float y1, float x2, float y2) {
		state = STATE.MOVING;
		distanceStart = calculateDistance(x1, y1, x2, y2);
    	dragStartX =offsetX+Math.min(x1, x2)/pixelsPerInterval*FloorModel.INTERVAL;
    	dragStartY = offsetY+Math.min(y1, y2)/pixelsPerInterval*FloorModel.INTERVAL;
    	setChanged();
		notifyObservers();
	}
	
	public void moveStop() {
		state = STATE.IDLE;
		setChanged();
		notifyObservers();
	}
	
	public void move(float x1, float y1, float x2, float y2) {
		double distanceMoved = calculateDistance(x1, y1, x2, y2);
		
		setPixelsPerInterval(pixelsPerInterval/(float)(distanceStart/distanceMoved));
		distanceStart = distanceMoved;
		
		setOffsetX(dragStartX-Math.min(x1, x2)/pixelsPerInterval*FloorModel.INTERVAL);
		setOffsetY(dragStartY-Math.min(y1, y2)/pixelsPerInterval*FloorModel.INTERVAL);
		setChanged();
		notifyObservers();
	}
	
	public boolean isMoving(){
		return state.equals(STATE.MOVING);
	}


	/**
	 * @return the viewWidth
	 */
	public int getViewWidth() {
		return viewWidth;
	}

	public void setViewSize(int width, int height) {
		this.viewWidth = width;
		this.viewHeight = height;
		setChanged();
		notifyObservers();
	}
	
}
