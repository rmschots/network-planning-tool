package com.ugent.networkplanningtool.data;

import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.utils.Utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

public class DataActivity extends DataObject{
	
	private ActivityType type;
	
	public DataActivity(DataActivity da){
		super(da);
		this.type = da.type;
	}

	public DataActivity(ActivityType type) {
		super();
		this.type = type;
	}
	
	public DataActivity(Point point, ActivityType type) {
		super(point);
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public ActivityType getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(ActivityType type) {
		this.type = type;
	}

	public int getColor(){
		return type.equals(ActivityType.NO_COVERAGE)?Color.RED:Color.GREEN;
	}

	@Override
	public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
		
		float circleRadius = drawingModel.getPixelsPerInterval()/3;
		float rectHeight = circleRadius/2;
		
		float x = convertCoordinateToLocation(drawingModel, true, getPoint1().x);
		float y = convertCoordinateToLocation(drawingModel, false, getPoint1().y);
		
		String textToDraw = getType().getText();
		int textSize = Utils.determineMaxTextSize(textToDraw, rectHeight*2*2/3, type.getTextSize());
		type.setTextSize(textSize);
		paint.setStyle(Style.FILL);
		paint.setTextSize(textSize);
		canvas.drawRect(x, y-rectHeight, x+circleRadius+paint.measureText(textToDraw)+rectHeight/2, y+rectHeight, paint);
		
		paint.setStyle(Style.FILL);
		paint.setColor(getColor());
		canvas.drawCircle(x, y, circleRadius, paint);
		paint.setStrokeWidth(drawingModel.getPixelsPerInterval()/4);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.BLACK);
		canvas.drawCircle(x, y, circleRadius, paint);
		
		paint.setStyle(Style.FILL);
		paint.setTextAlign(Align.LEFT);
		paint.setColor(Color.WHITE);
		canvas.drawText(textToDraw, x+circleRadius, y+rectHeight*2/3-paint.descent()/2, paint);
		
		if(touch){
			paint.setStyle(Style.STROKE);
			paint.setColor(Color.RED);
			paint.setPathEffect(dottedLineEffect);
			canvas.drawCircle(x, y, circleRadius, paint);
		}
		paint.reset();
	}

	@Override
	public DataObject getPartialDeepCopy() {
		return new DataActivity(type);
	}
}
