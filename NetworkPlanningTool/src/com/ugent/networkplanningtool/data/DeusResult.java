package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RadialGradient;

import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.utils.Utils;

public class DeusResult extends DataObject{
	
	private final int level;
	private final int download; // (mbps)
	private final int upload; // (mbps)
	private final double pathloss; // (db)
	private final double powerRX; // (db)
	private final double powerTX; // (db)
	private final double absorption; //SAR W/kg?
	private final double eField; // veldsterkte V/m
//	private final double pdLos;
//	private final double pdDif;
	private final int roomNumber;
	private final int drawingSize; // non-relevant; always squares of 10cm; don't draw if 0
	
	public DeusResult(int level, Point point, int download,
			int upload, double pathloss, double powerRX, double powerTX,
			double absorption, double eField, int roomNumber, int drawingSize) {
		super(point);
		this.level = level;
		this.download = download;
		this.upload = upload;
		this.pathloss = pathloss;
		this.powerRX = powerRX;
		this.powerTX = powerTX;
		this.absorption = absorption;
		this.eField = eField;
		this.roomNumber = roomNumber;
		this.drawingSize = drawingSize;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the download
	 */
	public int getDownload() {
		return download;
	}

	/**
	 * @return the upload
	 */
	public int getUpload() {
		return upload;
	}

	/**
	 * @return the pathloss
	 */
	public double getPathloss() {
		return pathloss;
	}

	/**
	 * @return the powerRX
	 */
	public double getPowerRX() {
		return powerRX;
	}

	/**
	 * @return the powerTX
	 */
	public double getPowerTX() {
		return powerTX;
	}

	/**
	 * @return the absorption
	 */
	public double getAbsorption() {
		return absorption;
	}

	/**
	 * @return the eField
	 */
	public double geteField() {
		return eField;
	}

	/**
	 * @return the roomNumber
	 */
	public int getRoomNumber() {
		return roomNumber;
	}

	/**
	 * @return the drawingSize
	 */
	public int getDrawingSize() {
		return drawingSize;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OptimizeResult [level=" + level + ", download=" + download
				+ ", upload=" + upload + ", pathloss=" + pathloss
				+ ", powerRX=" + powerRX + ", powerTX=" + powerTX
				+ ", absorption=" + absorption + ", eField=" + eField
				+ ", roomNumber=" + roomNumber + ", drawingSize=" + drawingSize
				+ ", point1=" + point1 + "]";
	}

	@Override
	public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
		float circleRadius = drawingModel.getPixelsPerInterval()/3;
		float rectHeight = circleRadius/2;
		
		float x = convertCoordinateToLocation(drawingModel, true, point1.x);
		float y = convertCoordinateToLocation(drawingModel, false, point1.y);
		
		//paint.setStyle(Style.FILL);
		RadialGradient gradient = new RadialGradient(x, y, circleRadius, Color.rgb((int)(eField*50),(int)(eField*50),(int)(eField*50)),
				Color.TRANSPARENT, android.graphics.Shader.TileMode.CLAMP);
		paint.setDither(true);
		paint.setShader(gradient);
		canvas.drawCircle(x, y, circleRadius, paint);
		paint.setStrokeWidth(drawingModel.getPixelsPerInterval()/4);
		
		paint.reset();
	}

	@Override
	public DataObject getPartialDeepCopy() {
		// TODO Auto-generated method stub
		return null;
	}

}
