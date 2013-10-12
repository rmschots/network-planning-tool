package com.ugent.networkplanningtool.model;

public class FloorModel {
	
	public static final float INTERVAL = 0.5f;
	public static final float FLOOR_WIDTH = 100f;
	public static final float FLOOR_HEIGHT = 75f;
	
	private float offsetX;
	private float offsetY;
	private float pixelsPerInterval;
	
	
	public FloorModel(){
		offsetX = 0;
		offsetY = 0;
		pixelsPerInterval = 100;
	}


	public float getOffsetX() {
		return offsetX;
	}


	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX<0?0:offsetX;
	}


	public float getOffsetY() {
		return offsetY;
	}


	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY<0?0:offsetY;
	}


	public float getPixelsPerInterval() {
		return pixelsPerInterval;
	}


	public void setPixelsPerInterval(float pixelsPerInterval) {
		this.pixelsPerInterval = pixelsPerInterval < 30 ? 30 : pixelsPerInterval;
	}
	
	
}
