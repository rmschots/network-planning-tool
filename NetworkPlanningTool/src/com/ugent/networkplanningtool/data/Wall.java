package com.ugent.networkplanningtool.data;

import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.utils.Utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;

public class Wall extends DataObject{
	
	private Point point2;
	
	private WallType wallType;
	private Thickness thickness;
	
	private Material material;
	
	public Wall(WallType wallType, Thickness thickness, Material material) {
		super();
		this.wallType = wallType;
		this.thickness = thickness;
		this.material = material;
	}
	
	public Wall(Point point1, WallType wallType, Thickness thickness, Material material) {
		super(point1);
		this.wallType = wallType;
		this.thickness = thickness;
		this.material = material;
	}

	public Wall(Point point1, Point point2, WallType wallType, Thickness thickness, Material material){
		this(point1, wallType, thickness, material);
		this.point2 = point2;
	}

	/**
	 * @return the x2
	 */
	public Point getPoint2() {
		return point2;
	}

	/**
	 * @param x2 the x2 to set
	 */
	public void setPoint2(Point point2) {
		this.point2 = point2;
	}
	
	public void setPoint2(int x, int y){
		if(x >= 0 && y >= 0){
			if(point2 == null){
				point2 = new Point(x, y);
			}else{
				point2.set(x, y);
			}
		}
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
				&& getPoint2() != null
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
		
		float pixelsX1 = convertCoordinateToLocation(drawingModel, true, getPoint1().x);
		float pixelsY1 = convertCoordinateToLocation(drawingModel, false, getPoint1().y);
		float circleRadius = drawingModel.getPixelsPerInterval()/4;
		if(isComplete()){
			float pixelsX2 = convertCoordinateToLocation(drawingModel, true, getPoint2().x);
			float pixelsY2 = convertCoordinateToLocation(drawingModel, false, getPoint2().y);
			
			paint.setStrokeWidth(drawingModel.getPixelsPerInterval()*thickness.getNumber()/DrawingModel.INTERVAL);
			paint.setColor(getMaterial().getColor());
			canvas.drawLine(pixelsX1, pixelsY1, pixelsX2, pixelsY2, paint);
			
			paint.setStrokeWidth(0);
			paint.setColor(Color.BLACK); 
			
			canvas.drawRect(pixelsX1-circleRadius, pixelsY1-circleRadius, pixelsX1+circleRadius, pixelsY1+circleRadius, paint);
			canvas.drawRect(pixelsX2-circleRadius, pixelsY2-circleRadius, pixelsX2+circleRadius, pixelsY2+circleRadius, paint);
			
			String textToDraw = Math.round(Utils.getDistance(getPoint1().x, getPoint1().y, getPoint2().x, getPoint2().y))/100.0+" m";
			paint.setTextSize(20);
			if(paint.measureText(textToDraw) < drawingModel.getPixelsPerInterval()*2){
				paint.setTextAlign(Align.CENTER);
				float textX = (pixelsX2+pixelsX1)/2;
				float textY = ((pixelsY2+pixelsY1)/2 - ((paint.descent() + paint.ascent()) / 2)) ;;
				float distanceCM = Utils.getDistance(getPoint1().x, getPoint1().y, getPoint2().x, getPoint2().y);
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
		return new Wall(wallType, thickness, material);
	}
}
