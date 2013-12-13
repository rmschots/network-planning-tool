package com.ugent.networkplanningtool.data;

import com.ugent.networkplanningtool.model.DrawingModel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;

public class ConnectionPoint extends DataObject{
	
	private ConnectionPointType type;
	
	public ConnectionPoint(ConnectionPoint cp){
		super(cp);
		this.type = cp.type;
	}
	
	public ConnectionPoint(ConnectionPointType type) {
		super();
		this.type = type;
	}

	public ConnectionPoint(Point point, ConnectionPointType type) {
		super(point);
		this.type = type;
	}

	@Override
	public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
		float circleRadius = drawingModel.getPixelsPerInterval()/6;
		float x = convertCoordinateToLocation(drawingModel, true, getPoint1().x);
		float y = convertCoordinateToLocation(drawingModel, false, getPoint1().y);
		paint.setStyle(Style.FILL);
		paint.setColor(type.getColor());
		canvas.drawCircle(x, y, circleRadius, paint);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(drawingModel.getPixelsPerInterval()/16);
		paint.setColor(Color.BLACK);
		canvas.drawCircle(x, y, circleRadius, paint);
		if(touch){
			paint.setColor(Color.RED);
			paint.setPathEffect(dottedLineEffect);
			canvas.drawCircle(x, y, circleRadius, paint);
		}
		paint.reset();
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
