package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;

import com.ugent.networkplanningtool.data.enums.Material;
import com.ugent.networkplanningtool.data.enums.Thickness;
import com.ugent.networkplanningtool.data.enums.WallType;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.utils.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A Wall object can represent a wall, door or window.
 */
public class Wall extends FloorPlanObject implements XMLTransformable {

    private Point point2;

    private WallType wallType;
    private Thickness thickness;

    private Material material;

    /**
     * Constructor creating a deep copy of the given Wall
     * @param wall wall to make a deep copy of
     */
    public Wall(Wall wall) {
        super(wall);
        DATA_OBJECT_TYPE = FloorPlanObjectType.WALL;
        this.point2 = new Point(wall.point2);
        this.wallType = wall.wallType;
        this.thickness = wall.thickness;
        this.material = wall.material;
    }

    /**
     * Creates a wall using its static properties (without edges)
     * @param wallType the type of the wall
     * @param thickness the thickness of the wall
     * @param material the material the wall is made of
     */
    public Wall(WallType wallType, Thickness thickness, Material material) {
        super();
        DATA_OBJECT_TYPE = FloorPlanObjectType.WALL;
        this.wallType = wallType;
        this.thickness = thickness;
        this.material = material;
        point2 = null;
    }

    /**
     * Creates a wall using its static properties and one edge
     * @param point1 the first edge of the wall
     * @param wallType the type of the wall
     * @param thickness the thickness of the wall
     * @param material the material the wall is made of
     */
    public Wall(Point point1, WallType wallType, Thickness thickness, Material material) {
        super(point1);
        DATA_OBJECT_TYPE = FloorPlanObjectType.WALL;
        this.wallType = wallType;
        this.thickness = thickness;
        this.material = material;
        point2 = null;
    }

    /**
     * Creates a wall using its static properties and its two edges
     * @param point1 the first edge of the wall
     * @param point2 the second edge of the wall
     * @param wallType the type of the wall
     * @param thickness the thickness of the wall
     * @param material the material the wall is made of
     */
    public Wall(Point point1, Point point2, WallType wallType, Thickness thickness, Material material) {
        this(point1, wallType, thickness, material);
        DATA_OBJECT_TYPE = FloorPlanObjectType.WALL;
        this.point2 = point2;
    }

    /**
     * Returns the second edge
     * @return the second edge
     */
    public Point getPoint2() {
        return point2;
    }

    /**
     * Sets the second edge
     * @param point2 the second edge
     */
    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    /**
     * Sets the second edge
     * @param x x coordinate of second edge
     * @param y y coordinate of second edge
     */
    public void setPoint2(int x, int y) {
        if (x >= 0 && y >= 0) {
            if (point2 == null) {
                point2 = new Point(x, y);
            } else {
                point2.set(x, y);
            }
        }
    }

    /**
     * Returns the type of the Wall
     * @return the type of the Wall
     */
    public WallType getWallType() {
        return wallType;
    }

    /**
     * Sets the type of the Wall
     * @param wallType the type of the Wall
     */
    public void setWallType(WallType wallType) {
        if (wallType != null) {
            this.wallType = wallType;
        }
    }

    /**
     * Returns the thickness of the Wall
     * @return the thickness of the Wall
     */
    public Thickness getThickness() {
        return thickness;
    }

    /**
     * Sets the thickness of the Wall
     * @param thickness the thickness of the Wall
     */
    public void setThickness(Thickness thickness) {
        if (thickness != null) {
            this.thickness = thickness;
        }
    }

    /**
     * Returns the material of the Wall
     * @return the material of the Wall
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the Wall
     * @param material the material of the Wall
     */
    public void setMaterial(Material material) {
        if (material != null) {
            this.material = material;
        }
    }

    @Override
    public boolean isComplete() {
        return super.isComplete()
                && canDraw()
                && point2 != null;
    }

    @Override
    public boolean canDraw() {
        return super.canDraw()
                && material != null
                && thickness != null
                && wallType != null;
    }

    @Override
    public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
        paint.reset();
        float pixelsX1 = drawingModel.convertCoordinateToLocation(true, point1.x);
        float pixelsY1 = drawingModel.convertCoordinateToLocation(false, point1.y);
        float circleRadius = drawingModel.getPixelsPerInterval() / 4;
        if (isComplete()) {
            float pixelsX2 = drawingModel.convertCoordinateToLocation(true, point2.x);
            float pixelsY2 = drawingModel.convertCoordinateToLocation(false, point2.y);


            paint.setStrokeWidth(drawingModel.getPixelsPerInterval() * thickness.getNumber() / DrawingModel.INTERVAL);
            paint.setColor(material.getColor());
            canvas.drawLine(pixelsX1, pixelsY1, pixelsX2, pixelsY2, paint);

            if (touch) {
                paint.setStrokeWidth(paint.getStrokeWidth() / 2);
                paint.setColor(Color.argb(100, 255, 255, 255));

                canvas.drawLine(pixelsX1, pixelsY1, pixelsX2, pixelsY2, paint);
            }

            paint.reset();
            paint.setStrokeWidth(0);
            paint.setColor(Color.BLACK);

            canvas.drawRect(pixelsX1 - circleRadius, pixelsY1 - circleRadius, pixelsX1 + circleRadius, pixelsY1 + circleRadius, paint);
            canvas.drawRect(pixelsX2 - circleRadius, pixelsY2 - circleRadius, pixelsX2 + circleRadius, pixelsY2 + circleRadius, paint);

            if (touch) {
                paint.setColor(Color.argb(100, 255, 255, 255));
                canvas.drawRect(pixelsX1 - circleRadius, pixelsY1 - circleRadius, pixelsX1 + circleRadius, pixelsY1 + circleRadius, paint);
                canvas.drawRect(pixelsX2 - circleRadius, pixelsY2 - circleRadius, pixelsX2 + circleRadius, pixelsY2 + circleRadius, paint);
            }
            if (drawingModel.isDrawLabels()) {
                String textToDraw = Math.round(Utils.pointToPointDistance(point1, point2)) / 100.0 + " m";
                paint.setTextSize(20);
                if (paint.measureText(textToDraw) < drawingModel.getPixelsPerInterval() * 2) {
                    paint.setTextAlign(Align.CENTER);
                    paint.setColor(Color.BLACK);
                    float textX = (pixelsX2 + pixelsX1) / 2;
                    float textY = ((pixelsY2 + pixelsY1) / 2 - ((paint.descent() + paint.ascent()) / 2));
                    double distanceCM = Utils.pointToPointDistance(point1, point2);
                    canvas.drawText(Math.round(distanceCM) / 100.0 + " m", textX, textY, paint);
                }
            }
        } else {
            paint.reset();
            paint.setStrokeWidth(0);
            paint.setColor(Color.BLACK);

            canvas.drawRect(pixelsX1 - circleRadius, pixelsY1 - circleRadius, pixelsX1 + circleRadius, pixelsY1 + circleRadius, paint);
            if (touch) {
                paint.setColor(Color.argb(100, 255, 255, 255));
                canvas.drawRect(pixelsX1 - circleRadius, pixelsY1 - circleRadius, pixelsX1 + circleRadius, pixelsY1 + circleRadius, paint);
            }
        }
        paint.reset();
    }

    @Override
    public FloorPlanObject getPartialDeepCopy() {
        return new Wall(wallType, thickness, material);
    }

    @Override
    public Element toXML(Document doc) {
        Element wallElement = doc.createElement("wall");
        wallElement.setAttribute("x1", "" + point1.x);
        wallElement.setAttribute("y1", "" + point1.y);
        wallElement.setAttribute("x2", "" + point2.x);
        wallElement.setAttribute("y2", "" + point2.y);
        wallElement.setAttribute("type", wallType == null ? "" : wallType.getText());
        wallElement.setAttribute("thickness", thickness == null ? "" : "" + thickness.getNumber());
        Element materialElement = doc.createElement("material");
        materialElement.setAttribute("name", material == null ? "" : material.getText());
        wallElement.appendChild(materialElement);
        return wallElement;
    }

    /**
     * Returns whether the given Wall is at the same location
     * @param wall2 the Wall to compare to
     * @return whether the given Wall is at the same location
     */
    public boolean equalsLocation(Wall wall2) {
        return (point1.equals(wall2.point1) && point2.equals(wall2.point2))
                || (point1.equals(wall2.point2) && point2.equals(wall2.point1));
    }
}
