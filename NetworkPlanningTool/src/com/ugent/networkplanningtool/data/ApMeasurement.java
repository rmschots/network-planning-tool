package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.utils.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ApMeasurement extends FloorPlanObject implements XMLTransformable {
    private Integer signalStrength = null;

    /**
     * The default sample pool size
     */
    public static int DEFAULT_SAMPLE_POOL_SIZE = 2;
    private int samplePoolSize;
    private static int textSize = 1;

    /**
     * Constructor setting the sample pool size
     * @param samplePoolSize the sample pool size
     */
    public ApMeasurement(int samplePoolSize) {
        super();
        DATA_OBJECT_TYPE = FloorPlanObjectType.AP_MEASUREMENT;
        this.samplePoolSize = samplePoolSize;
    }

    /**
     * Constructor setting the location
     * @param p1 the location
     */
    public ApMeasurement(Point p1) {
        super(p1);
        DATA_OBJECT_TYPE = FloorPlanObjectType.AP_MEASUREMENT;
        samplePoolSize = DEFAULT_SAMPLE_POOL_SIZE;
    }

    /**
     * Constructor setting the location and measured average signal strength
     * @param p1 the location
     * @param signalStrength the measured average signal strength
     */
    public ApMeasurement(Point p1, int signalStrength) {
        this(p1);
        this.signalStrength = signalStrength;
    }

    /**
     * Returns the signal strength
     * @return the signal strength
     */
    public Integer getSignalStrength() {
        return signalStrength;
    }

    /**
     * Sets the signal strength
     * @param signalStrength the signal strength
     */
    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    /**
     * Returns sample pool size
     * @return sample pool size
     */
    public int getSamplePoolSize() {
        return samplePoolSize;
    }

    /**
     * Sets sample pool size
     * @param samplePoolSize sample pool size
     */
    public void setSamplePoolSize(int samplePoolSize) {
        this.samplePoolSize = samplePoolSize;
    }

    @Override
    public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
        float circleRadius = drawingModel.getPixelsPerInterval() / 6;
        float x = drawingModel.convertCoordinateToLocation(true, point1.x);
        float y = drawingModel.convertCoordinateToLocation(false, point1.y);
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
    public FloorPlanObject getPartialDeepCopy() {
        return new ApMeasurement(samplePoolSize);
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
