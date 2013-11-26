package com.ugent.networkplanningtool.data;

import com.ugent.networkplanningtool.model.DrawingModel;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class DataObject{
	
	private int x1 = -1;
	private int y1 = -1;

	public DataObject(int x1, int y1) {
		this.x1 = x1;
		this.y1 = y1;
	}
	
	/**
	 * @return the x1
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * @param x1 the x1 to set
	 */
	public void setX1(int x1) {
		this.x1 = x1;
	}

	/**
	 * @return the y1
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * @param y1 the y1 to set
	 */
	public void setY1(int y1) {
		this.y1 = y1;
	}
	
	public abstract void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint);
	
	public boolean hasEnoughData(){
		return x1 != -1 && y1 != -1;
	}
	
	public abstract DataObject deepCopy();
	
	protected float convertCoordinateToLocation(DrawingModel drawingModel, boolean x, float coordinate){
		return (coordinate-(x?drawingModel.getOffsetX():drawingModel.getOffsetY()))
				*drawingModel.getPixelsPerInterval()/DrawingModel.INTERVAL;
	}
}
