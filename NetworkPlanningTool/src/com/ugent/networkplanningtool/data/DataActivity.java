package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;

import com.ugent.networkplanningtool.data.enums.ActivityType;
import com.ugent.networkplanningtool.model.DrawingModel;
import com.ugent.networkplanningtool.utils.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DataActivity extends FloorPlanObject implements XMLTransformable {

    private ActivityType type;

    public DataActivity(DataActivity da) {
        super(da);
        DATA_OBJECT_TYPE = DataObjectType.DATA_ACTIVITY;
        this.type = da.type;
    }

    public DataActivity(ActivityType type) {
        super();
        DATA_OBJECT_TYPE = DataObjectType.DATA_ACTIVITY;
        this.type = type;
    }

    public DataActivity(Point point, ActivityType type) {
        super(point);
        DATA_OBJECT_TYPE = DataObjectType.DATA_ACTIVITY;
        this.type = type;
    }

    /**
     * @return the type
     */
    public ActivityType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ActivityType type) {
        this.type = type;
    }

    public int getColor() {
        return type.equals(ActivityType.NO_COVERAGE) ? Color.RED : Color.GREEN;
    }

    @Override
    public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {

        float circleRadius = drawingModel.getPixelsPerInterval() / 3;
        float rectHeight = circleRadius / 2;

        float x = convertCoordinateToLocation(drawingModel, true, point1.x);
        float y = convertCoordinateToLocation(drawingModel, false, point1.y);

        String textToDraw = type.getText();
        int textSize = Utils.determineMaxTextSize(textToDraw, rectHeight * 2 * 2 / 3, type.getTextSize());
        type.setTextSize(textSize);
        paint.setStyle(Style.FILL);
        paint.setTextSize(textSize);
        canvas.drawRect(x, y - rectHeight, x + circleRadius + paint.measureText(textToDraw) + rectHeight / 2, y + rectHeight, paint);

        paint.setStyle(Style.FILL);
        paint.setColor(getColor());
        canvas.drawCircle(x, y, circleRadius, paint);
        paint.setStrokeWidth(drawingModel.getPixelsPerInterval() / 4);
        paint.setStyle(Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(x, y, circleRadius, paint);

        paint.setStyle(Style.FILL);
        paint.setTextAlign(Align.LEFT);
        paint.setColor(Color.WHITE);
        canvas.drawText(textToDraw, x + circleRadius, y + rectHeight * 2 / 3 - paint.descent() / 2, paint);

        if (touch) {
            paint.setStyle(Style.STROKE);
            paint.setColor(Color.RED);
            paint.setPathEffect(dottedLineEffect);
            canvas.drawCircle(x, y, circleRadius, paint);
        }
        paint.reset();
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
        return new DataActivity(type);
    }

    @Override
    public Element toXML(Document doc) {
        Element apElement = doc.createElement("activity");
        apElement.setAttribute("x", "" + point1.x);
        apElement.setAttribute("y", "" + point1.y);
        apElement.setAttribute("type", type.getText());
        return apElement;
    }
}
