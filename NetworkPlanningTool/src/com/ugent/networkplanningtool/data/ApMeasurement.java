package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.utils.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by Roel on 24/03/14.
 */
public class ApMeasurement extends DataObject implements XMLTransformable {
    private Integer signalStrength = null;
    private int samplePoolSize = 5;
    private static int textSize = 1;

    public ApMeasurement() {
        super();
        DATA_OBJECT_TYPE = DataObjectType.AP_MEASUREMENT;
    }

    public ApMeasurement(Point p1) {
        super(p1);
        DATA_OBJECT_TYPE = DataObjectType.AP_MEASUREMENT;
    }

    public Integer getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    public int getSamplePoolSize() {
        return samplePoolSize;
    }

    public void setSamplePoolSize(int samplePoolSize) {
        this.samplePoolSize = samplePoolSize;
    }

    @Override
    public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
        float circleRadius = drawingModel.getPixelsPerInterval() / 6;
        float x = convertCoordinateToLocation(drawingModel, true, point1.x);
        float y = convertCoordinateToLocation(drawingModel, false, point1.y);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, circleRadius, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(drawingModel.getPixelsPerInterval() / 16);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(x, y, circleRadius, paint);
        if (touch) {
            paint.setColor(Color.RED);
            paint.setPathEffect(dottedLineEffect);
            canvas.drawCircle(x, y, circleRadius, paint);
        }
        if (signalStrength != null) {
            paint.reset();
            paint.setColor(Color.CYAN);
            paint.setTextAlign(Paint.Align.CENTER);
            textSize = Utils.determineMaxTextSize("" + signalStrength, circleRadius, textSize);
            paint.setTextSize(textSize);
            canvas.drawText("" + signalStrength, x, y + (circleRadius / 2), paint);
        }

        paint.reset();
    }

    @Override
    public DataObject getPartialDeepCopy() {
        ApMeasurement apm = new ApMeasurement();
        apm.setSamplePoolSize(samplePoolSize);
        return apm;
    }

    @Override
    public Element toXML(Document doc) {
        Element apMElement = doc.createElement("measurement");
        apMElement.setAttribute("x", "" + point1.x);
        apMElement.setAttribute("y", "" + point1.y);
        apMElement.setAttribute("rssi", "" + signalStrength);
        return apMElement;
    }
}
