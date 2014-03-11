package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Point;

import com.ugent.networkplanningtool.model.DrawingModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class DataObject{

    public static enum DataObjectType{
		WALL,
		ACCESS_POINT,
		DATA_ACTIVITY,
		CONNECTION_POINT
	}
	
	public DataObjectType DATA_OBJECT_TYPE;
	
	protected static PathEffect dottedLineEffect = new DashPathEffect(new float[] {10,5}, 0);
	protected Point point1;
	
	public DataObject(){
		point1 = null;
	}
	
	public DataObject(DataObject dataObject){
		this.point1 = new Point(dataObject.point1);
	}

	public DataObject(Point point) {
		this.point1 = point;
	}
	
	/**
	 * @return the x1
	 */
	public Point getPoint1() {
		return point1;
	}

	/**
	 * @param point1 the point1 to set
	 */
	public void setPoint1(Point point1) {
		this.point1 = point1;
	}
	
	public void setPoint1(int x, int y){
		if(x >= 0 && y >= 0){
			if(point1 == null){
				point1 = new Point(x, y);
			}else{
				point1.set(x, y);
			}
		}
	}
	
	public abstract void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch);
	
	public boolean isComplete(){
		return canDraw();
	}
	
	public boolean canDraw(){
		return point1 != null;
	}
	
	public abstract DataObject getPartialDeepCopy();
	
	protected float convertCoordinateToLocation(DrawingModel drawingModel, boolean isXCoord, float coordinate){
		return (coordinate-(isXCoord?drawingModel.getOffsetX():drawingModel.getOffsetY()))
				*drawingModel.getPixelsPerInterval()/DrawingModel.INTERVAL;
	}
}
