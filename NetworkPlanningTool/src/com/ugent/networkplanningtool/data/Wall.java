package com.ugent.networkplanningtool.data;

import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.utils.Utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class Wall extends DataObject{
	
	private int x2 = -1;
	private int y2 = -1;
	
	private WallType wallType;
	private Thickness thickness;
	
	private Material material;
	
	
	public Wall(int x1, int y1, WallType wallType, Thickness thickness, Material material) {
		super(x1,y1);
		this.wallType = wallType;
		this.thickness = thickness;
		this.material = material;
	}

	public Wall(int x1, int y1, int x2, int y2, WallType wallType, Thickness thickness, Material material){
		this(x1, y1, wallType, thickness, material);
		this.x2 = x2;
		this.y2 = y2;
	}

	/**
	 * @return the x2
	 */
	public int getX2() {
		return x2;
	}

	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(int x2) {
		this.x2 = x2;
	}
	
	/**
	 * @return the y2
	 */
	public int getY2() {
		return y2;
	}

	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(int y2) {
		this.y2 = y2;
	}

	
	/**
	 * @return the wallType
	 */
	public WallType getWallType() {
		return wallType;
	}

	/**
	 * @param wallType the wallType to set
	 */
	public void setWallType(WallType wallType) {
		if(wallType != null){
			this.wallType = wallType;
		}
	}

	/**
	 * @return the thickness
	 */
	public Thickness getThickness() {
		return thickness;
	}

	/**
	 * @param thickness the thickness to set
	 */
	public void setThickness(Thickness thickness) {
		if(thickness != null){
			this.thickness = thickness;
		}
	}

	/**
	 * @return the Material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @param color the Material to set
	 */
	public void setMaterial(Material material) {
		if(material != null){
			this.material = material;
		}
	}

	@Override
	public boolean isComplete(){
		return super.isComplete()
				&& getX2() != -1
				&& getY2() != -1
				&& getMaterial() != null
				&& getThickness() != null
				&& getWallType() != null;
	}
	
	public boolean canDraw(){
		return super.isComplete()
				&& getMaterial() != null
				&& getThickness() != null
				&& getWallType() != null;
	}

	@Override
	public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint) {
		
		float pixelsX1 = convertCoordinateToLocation(drawingModel, true, getX1());
		float pixelsY1 = convertCoordinateToLocation(drawingModel, false, getY1());
		float circleRadius = drawingModel.getPixelsPerInterval()/4;
		if(isComplete()){
			float pixelsX2 = convertCoordinateToLocation(drawingModel, true, getX2());
			float pixelsY2 = convertCoordinateToLocation(drawingModel, false, getY2());
			
			paint.setStrokeWidth(drawingModel.getPixelsPerInterval()*thickness.getNumber()/DrawingModel.INTERVAL);
			paint.setColor(getMaterial().getColor());
			canvas.drawLine(pixelsX1, pixelsY1, pixelsX2, pixelsY2, paint);
			
			paint.setStrokeWidth(0);
			paint.setColor(Color.BLACK); 
			
			canvas.drawRect(pixelsX1-circleRadius, pixelsY1-circleRadius, pixelsX1+circleRadius, pixelsY1+circleRadius, paint);
			canvas.drawRect(pixelsX2-circleRadius, pixelsY2-circleRadius, pixelsX2+circleRadius, pixelsY2+circleRadius, paint);
			
			String textToDraw = Math.round(Utils.getDistance(getX1(), getY1(), getX2(), getY2()))/100.0+" m";
			paint.setTextSize(20);
			if(paint.measureText(textToDraw) < drawingModel.getPixelsPerInterval()*2){
				paint.setTextAlign(Align.CENTER);
				float textX = (pixelsX2+pixelsX1)/2;
				float textY = ((pixelsY2+pixelsY1)/2 - ((paint.descent() + paint.ascent()) / 2)) ;;
				float distanceCM = Utils.getDistance(getX1(), getY1(), getX2(), getY2());
				canvas.drawText(Math.round(distanceCM)/100.0+" m", textX, textY, paint);
			}
			
		}else{
			paint.setStrokeWidth(0);
			paint.setColor(Color.BLACK); 
			
			canvas.drawRect(pixelsX1-circleRadius, pixelsY1-circleRadius, pixelsX1+circleRadius, pixelsY1+circleRadius, paint);
		}
	}

	@Override
	public DataObject getPartialDeepCopy() {
		return new Wall(-1, -1, wallType, thickness, material);
	}
}
