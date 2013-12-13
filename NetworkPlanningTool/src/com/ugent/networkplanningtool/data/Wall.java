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
	
	public Wall(Wall wall){
		super(wall);
		this.point2 = new Point(wall.point2);
		this.wallType = wall.wallType;
		this.thickness = wall.thickness;
		this.material = wall.material;
	}
	
	public Wall(WallType wallType, Thickness thickness, Material material) {
		super();
		this.wallType = wallType;
		this.thickness = thickness;
		this.material = material;
		point2 = null;
	}
	
	public Wall(Point point1, WallType wallType, Thickness thickness, Material material) {
		super(point1);
		this.wallType = wallType;
		this.thickness = thickness;
		this.material = material;
		point2 = null;
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
				&& point2 != null
				&& material != null
				&& thickness != null
				&& wallType != null;
	}
	
	public boolean canDraw(){
		return super.isComplete()
				&& material != null
				&& thickness != null
				&& wallType != null;
	}

	@Override
	public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
		
		float pixelsX1 = convertCoordinateToLocation(drawingModel, true, point1.x);
		float pixelsY1 = convertCoordinateToLocation(drawingModel, false, point1.y);
		float circleRadius = drawingModel.getPixelsPerInterval()/4;
		if(isComplete()){
			float pixelsX2 = convertCoordinateToLocation(drawingModel, true, point2.x);
			float pixelsY2 = convertCoordinateToLocation(drawingModel, false, point2.y);
			
			paint.setStrokeWidth(drawingModel.getPixelsPerInterval()*thickness.getNumber()/DrawingModel.INTERVAL);
			paint.setColor(material.getColor());
			canvas.drawLine(pixelsX1, pixelsY1, pixelsX2, pixelsY2, paint);
			
			paint.setStrokeWidth(0);
			if(touch){
				paint.setColor(Color.RED);
			}else{
				paint.setColor(Color.BLACK);
			}
			
			canvas.drawRect(pixelsX1-circleRadius, pixelsY1-circleRadius, pixelsX1+circleRadius, pixelsY1+circleRadius, paint);
			canvas.drawRect(pixelsX2-circleRadius, pixelsY2-circleRadius, pixelsX2+circleRadius, pixelsY2+circleRadius, paint);
			
			String textToDraw = Math.round(Utils.pointToPointDistance(point1, point2))/100.0+" m";
			paint.setTextSize(20);
			if(paint.measureText(textToDraw) < drawingModel.getPixelsPerInterval()*2){
				paint.setTextAlign(Align.CENTER);
				paint.setColor(Color.BLACK);
				float textX = (pixelsX2+pixelsX1)/2;
				float textY = ((pixelsY2+pixelsY1)/2 - ((paint.descent() + paint.ascent()) / 2)) ;;
				double distanceCM = Utils.pointToPointDistance(point1, point2);
				canvas.drawText(Math.round(distanceCM)/100.0+" m", textX, textY, paint);
			}
			
		}else{
			paint.setStrokeWidth(0);
			if(touch){
				paint.setColor(Color.RED);
			}else{
				paint.setColor(Color.BLACK);
			}
			
			canvas.drawRect(pixelsX1-circleRadius, pixelsY1-circleRadius, pixelsX1+circleRadius, pixelsY1+circleRadius, paint);
		}
	}

	@Override
	public DataObject getPartialDeepCopy() {
		return new Wall(wallType, thickness, material);
	}
	
	public boolean equalsLocation(Wall wall2){
			return (point1.equals(wall2.point1) && point2.equals(wall2.point2))
					|| (point1.equals(wall2.point2) && point2.equals(wall2.point1));
	}
}
