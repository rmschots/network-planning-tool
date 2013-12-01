package com.ugent.networkplanningtool.data;

import com.ugent.networkplanningtool.model.DrawingModel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class ConnectionPoint extends DataObject{
	
	private ConnectionPointType type;
	
	public ConnectionPoint(ConnectionPointType type) {
		super();
		this.type = type;
	}

	public ConnectionPoint(Point point, ConnectionPointType type) {
		super(point);
		this.type = type;
	}

	@Override
	public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint) {
		float circleRadius1 = drawingModel.getPixelsPerInterval()/6;
		float circleRadius2 = circleRadius1*4/6;
			float pixelsX1 = convertCoordinateToLocation(drawingModel, true, getPoint1().x);
			float pixelsY1 = convertCoordinateToLocation(drawingModel, false, getPoint1().y);
			paint.setColor(Color.BLACK);
			canvas.drawCircle(pixelsX1, pixelsY1, circleRadius1, paint);
			paint.setColor(type.getColor());
			canvas.drawCircle(pixelsX1, pixelsY1, circleRadius2, paint);
	}
	
	public ConnectionPointType getType() {
		return type;
	}

	public void setType(ConnectionPointType type) {
		if(type != null){
			this.type = type;
		}
	}

	@Override
	public DataObject getPartialDeepCopy() {
		return new ConnectionPoint(type);
	}

}
