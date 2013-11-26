package com.ugent.networkplanningtool.data;

import com.ugent.networkplanningtool.model.DrawingModel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ConnectionPoint extends DataObject{

	public ConnectionPoint(int x, int y) {
		super(x,y);
	}

	@Override
	public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint) {
		float circleRadius1 = drawingModel.getPixelsPerInterval()/6;
		float circleRadius2 = circleRadius1*4/6;
			float pixelsX1 = convertCoordinateToLocation(drawingModel, true, getX1());
			float pixelsY1 = convertCoordinateToLocation(drawingModel, false, getY1());
			paint.setColor(Color.BLACK);
			canvas.drawCircle(pixelsX1, pixelsY1, circleRadius1, paint);
			paint.setColor(Color.rgb(114, 15, 24));
			canvas.drawCircle(pixelsX1, pixelsY1, circleRadius2, paint);
	}

	@Override
	public DataObject deepCopy() {
		return new ConnectionPoint(getX1(), getY1());
	}

}
