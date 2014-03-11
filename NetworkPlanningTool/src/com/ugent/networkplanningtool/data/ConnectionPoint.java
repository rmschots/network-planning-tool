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

public class ConnectionPoint extends DataObject implements XMLTransformable{

    private ConnectionPointType type;

    public ConnectionPoint(ConnectionPoint cp) {
        super(cp);
        DATA_OBJECT_TYPE = DataObjectType.CONNECTION_POINT;
        this.type = cp.type;
    }

    public ConnectionPoint(ConnectionPointType type) {
        super();
        DATA_OBJECT_TYPE = DataObjectType.CONNECTION_POINT;
        this.type = type;
    }

    public ConnectionPoint(Point point, ConnectionPointType type) {
        super(point);
        DATA_OBJECT_TYPE = DataObjectType.CONNECTION_POINT;
        this.type = type;
    }

    @Override
    public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
        float circleRadius = drawingModel.getPixelsPerInterval() / 6;
        float x = convertCoordinateToLocation(drawingModel, true, point1.x);
        float y = convertCoordinateToLocation(drawingModel, false, point1.y);
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

    public ConnectionPointType getType() {
        return type;
    }

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
    public DataObject getPartialDeepCopy() {
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
