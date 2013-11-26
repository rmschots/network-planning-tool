package com.ugent.networkplanningtool.data;

import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.utils.Utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DataActivity extends DataObject{
	
	private ActivityType type;

	public DataActivity(int x, int y, ActivityType type) {
		super(x,y);
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public ActivityType getType() {
		return type;
	}
	
	public int getColor(){
		return type.equals(ActivityType.NO_COVERAGE)?Color.RED:Color.GREEN;
	}

	@Override
	public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint) {
		float circleRadius1 = drawingModel.getPixelsPerInterval()/3;
		float circleRadius2 = circleRadius1/2;
		
		float pixelsX1 = convertCoordinateToLocation(drawingModel, true, getX1());
		float pixelsY1 = convertCoordinateToLocation(drawingModel, false, getY1());
		paint.setColor(Color.BLACK);
		canvas.drawCircle(pixelsX1, pixelsY1, circleRadius1, paint);
		String textToDraw = getType().getText();
		paint.setTextSize(Utils.determineMaxTextSize(textToDraw, circleRadius2*2*2/3));
		canvas.drawRect(pixelsX1, pixelsY1-circleRadius2, pixelsX1+circleRadius1+paint.measureText(textToDraw)+circleRadius2/2, pixelsY1+circleRadius2, paint);
		paint.setColor(getColor());
		canvas.drawCircle(pixelsX1, pixelsY1, circleRadius2, paint);
		paint.setColor(Color.WHITE);
		canvas.drawText(textToDraw, pixelsX1+circleRadius1, pixelsY1+circleRadius2*2/3-paint.descent()/2, paint);
	}

	@Override
	public DataObject deepCopy() {
		return new DataActivity(getX1(), getY1(), type);
	}
}
