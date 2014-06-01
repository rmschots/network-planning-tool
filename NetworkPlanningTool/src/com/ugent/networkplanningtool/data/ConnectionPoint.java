package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;

import com.ugent.networkplanningtool.data.enums.ConnectionPointType;
import com.ugent.networkplanningtool.model.DrawingModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents a connection point for data or power
 */
public class ConnectionPoint extends FloorPlanObject implements XMLTransformable{

    private ConnectionPointType type;

    /**
     * Constructor creating a deep copy of another ConnectionPoint
     * @param cp the ConnectionPoint to create a deep copy of
     */
    public ConnectionPoint(ConnectionPoint cp) {
        super(cp);
        DATA_OBJECT_TYPE = FloorPlanObjectType.CONNECTION_POINT;
        this.type = cp.type;
    }

    /**
     * Constructor setting the connection point type
     * @param type the connection point type
     */
    public ConnectionPoint(ConnectionPointType type) {
        super();
        DATA_OBJECT_TYPE = FloorPlanObjectType.CONNECTION_POINT;
        this.type = type;
    }

    /**
     * Constructor setting the connection point type and location
     * @param point the location
     * @param type the connection point type
     */
    public ConnectionPoint(Point point, ConnectionPointType type) {
        super(point);
        DATA_OBJECT_TYPE = FloorPlanObjectType.CONNECTION_POINT;
        this.type = type;
    }

    @Override
    public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
        float circleRadius = drawingModel.getPixelsPerInterval() / 6;
        float x = drawingModel.convertCoordinateToLocation(true, point1.x);
        float y = drawingModel.convertCoordinateToLocation(false, point1.y);
        paint.setStyle(Style.FILL);
        paint.setColor(type.getColor());
        canvas.drawCircle(x, y, circleRadius, paint);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(drawingModel.getPixelsPerInterval() / 16);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(x, y, circleRadius, paint);
        if (touch) {
            paint.setColor(Color.RED);
            paint.setPathEffect(dottedLineEffect);
            canvas.drawCircle(x, y, circleRadius, paint);
        }
        paint.reset();
    }

    /**
     * Returns the type of the connection point
     * @return the type of the connection point
     */
    public ConnectionPointType getType() {
        return type;
    }

    /**
     * Sets the type of the connection point
     * @param type the type of the connection point
     */
    public void setType(ConnectionPointType type) {
        if (type != null) {
            this.type = type;
        }
    }

    @Override
    public boolean isComplete() {
        return super.isComplete()
                && canDraw();
    }

    @Override
    public boolean canDraw() {
        return super.canDraw()
                && type != null;
    }

    @Override
    public FloorPlanObject getPartialDeepCopy() {
        return new ConnectionPoint(type);
    }

    @Override
    public Element toXML(Document doc) {
        Element cpElement;
        if (type.equals(ConnectionPointType.DATA)) {
            cpElement = doc.createElement("dataconnpoint");
        } else {
            cpElement = doc.createElement("powerconnpoint");
        }
        cpElement.setAttribute("x", "" + point1.x);
        cpElement.setAttribute("y", "" + point1.y);
        return cpElement;
    }

}
