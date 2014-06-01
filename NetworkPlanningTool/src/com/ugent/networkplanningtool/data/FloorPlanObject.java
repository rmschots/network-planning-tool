package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Point;

import com.ugent.networkplanningtool.model.DrawingModel;

/**
 * Represents an object that can be drawn on the floor plan
 */
public abstract class FloorPlanObject {

    /**
     * The types of floor plan objects
     */
    public static enum FloorPlanObjectType {
        WALL,
        ACCESS_POINT,
        DATA_ACTIVITY,
        CONNECTION_POINT,
        AP_MEASUREMENT
    }

    /**
     * The type of this instance of FloorPlanObject
     */
    public FloorPlanObjectType DATA_OBJECT_TYPE;

    /**
     * Dotted line effect used for drawing certain objects
     */
    protected static PathEffect dottedLineEffect = new DashPathEffect(new float[]{10, 5}, 0);

    /**
     * All floor plan objects need al least one point (location) to draw
     */
    protected Point point1;

    /**
     * Default constructor
     */
    public FloorPlanObject() {
        point1 = null;
    }

    /**
     * Constructor for a deep copy of a given FloorPlanObject
     * Can be used by extending classes
     * @param floorPlanObject the FloorPlanObject to take a deep copy of
     */
    public FloorPlanObject(FloorPlanObject floorPlanObject) {
        this.point1 = new Point(floorPlanObject.point1);
    }

    /**
     * Constructor that sets the location of the object
     * Can be used by extending classes
     * @param point the location of the object
     */
    public FloorPlanObject(Point point) {
        this.point1 = point;
    }

    /**
     * Returns the location of the object
     * @return the location of the object
     */
    public Point getPoint1() {
        return point1;
    }

    /**
     * Sets the location of the object
     * @param point1 the location of the object
     */
    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    /**
     * Sets the location of the object using its coordinates
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setPoint1(int x, int y) {
        if (x >= 0 && y >= 0) {
            if (point1 == null) {
                point1 = new Point(x, y);
            } else {
                point1.set(x, y);
            }
        }
    }

    /**
     * Lets the object draw itself on the given canvas that belongs to a DrawingView
     * To be implemented by extending classes
     * @param canvas the canvas to draw on
     * @param drawingModel the DrawingModel used for information how to draw exactly
     * @param paint the Paint used for drawing styles and colors
     * @param touch whether the object is currently touched by the user on the screen
     */
    public abstract void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch);

    /**
     * Returns whether the object is ready to be added to the floor plan, and thus misses no required data
     * @return whether the object is ready to be added to the floor plan, and thus misses no required data
     */
    public boolean isComplete() {
        return canDraw();
    }

    /**
     * Returns whether the instance has sufficient data to be drawn on the floor plan
     * @return whether the instance has sufficient data to be drawn on the floor plan
     */
    public boolean canDraw() {
        return point1 != null;
    }

    /**
     * Creates a partial deep copy of the FloorPlanObject
     * To be implemented by extending classes
     * @return the partial deep copy
     */
    public abstract FloorPlanObject getPartialDeepCopy();
}
