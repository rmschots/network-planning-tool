package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Point;

import com.ugent.networkplanningtool.model.DrawingModel;

public abstract class FloorPlanObject {

    public static enum DataObjectType {
        WALL,
        ACCESS_POINT,
        DATA_ACTIVITY,
        CONNECTION_POINT,
        AP_MEASUREMENT
    }

    public DataObjectType DATA_OBJECT_TYPE;

    protected static PathEffect dottedLineEffect = new DashPathEffect(new float[]{10, 5}, 0);
    protected Point point1;

    public FloorPlanObject() {
        point1 = null;
    }

    public FloorPlanObject(FloorPlanObject floorPlanObject) {
        this.point1 = new Point(floorPlanObject.point1);
    }

    public FloorPlanObject(Point point) {
        this.point1 = point;
    }

    /**
     * @return the x1
     */
    public Point getPoint1() {
        return point1;
    }

    /**
     * @param point1 the point1 to set
     */
    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public void setPoint1(int x, int y) {
        if (x >= 0 && y >= 0) {
            if (point1 == null) {
                point1 = new Point(x, y);
            } else {
                point1.set(x, y);
            }
        }
    }

    public abstract void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch);

    public boolean isComplete() {
        return canDraw();
    }

    public boolean canDraw() {
        return point1 != null;
    }

    public abstract FloorPlanObject getPartialDeepCopy();
}