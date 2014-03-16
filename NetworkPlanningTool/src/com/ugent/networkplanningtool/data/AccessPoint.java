package com.ugent.networkplanningtool.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;

import com.ugent.networkplanningtool.data.enums.Frequency;
import com.ugent.networkplanningtool.data.enums.FrequencyBand;
import com.ugent.networkplanningtool.data.enums.Network;
import com.ugent.networkplanningtool.data.enums.RadioModel;
import com.ugent.networkplanningtool.data.enums.RadioType;
import com.ugent.networkplanningtool.model.DrawingModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AccessPoint extends DataObject implements XMLTransformable{

    private String name;
    private int height;

    private RadioType type;
    private RadioModel model;
    private Frequency frequency;
    private FrequencyBand frequencyband;
    private int gain;
    private double power;
    private Network network;

    private RealAccessPoint rap;

    public AccessPoint(AccessPoint accessPoint) {
        super(accessPoint);
        DATA_OBJECT_TYPE = DataObjectType.ACCESS_POINT;
        this.name = accessPoint.name;
        this.height = accessPoint.height;
        this.type = accessPoint.type;
        this.model = accessPoint.model;
        this.frequency = accessPoint.frequency;
        this.frequencyband = accessPoint.frequencyband;
        this.gain = accessPoint.gain;
        this.power = accessPoint.power;
        this.network = accessPoint.network;
    }

    public AccessPoint(String name, int height, RadioType type,
                       RadioModel model, FrequencyBand frequencyband, Frequency frequency, int gain,
                       double power, Network network) {
        super();
        DATA_OBJECT_TYPE = DataObjectType.ACCESS_POINT;
        this.name = name;
        this.height = height;
        this.type = type;
        this.model = model;
        this.frequency = frequency;
        this.frequencyband = frequencyband;
        this.gain = gain;
        this.power = power;
        this.network = network;
    }

    public AccessPoint(Point point, String name, int height, RadioType type,
                       RadioModel model, FrequencyBand frequencyband, Frequency frequency, int gain,
                       double power, Network network) {
        super(point);
        DATA_OBJECT_TYPE = DataObjectType.ACCESS_POINT;
        this.name = name;
        this.height = height;
        this.type = type;
        this.model = model;
        this.frequency = frequency;
        this.frequencyband = frequencyband;
        this.gain = gain;
        this.power = power;
        this.network = network;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public RadioType getType() {
        return type;
    }

    public void setType(RadioType type) {
        this.type = type;
    }

    public RadioModel getModel() {
        return model;
    }

    public void setModel(RadioModel model) {
        this.model = model;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        System.out.println("freq set to " + frequency);
        this.frequency = frequency;
    }

    public FrequencyBand getFrequencyband() {
        return frequencyband;
    }

    public void setFrequencyband(FrequencyBand frequencyband) {
        this.frequencyband = frequencyband;
    }

    public int getGain() {
        return gain;
    }

    public void setGain(int gain) {
        this.gain = gain;
    }

    public double getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public RealAccessPoint getRap() {
        return rap;
    }

    public void setRap(RealAccessPoint rap) {
        this.rap = rap;
    }

    @Override
    public boolean isComplete() {
        return super.isComplete()
                && canDraw();
    }

    @Override
    public boolean canDraw() {
        return super.canDraw()
                && name != null
                && type != null
                && model != null;
    }

    @Override
    public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
        // TODO display gain+power
        float x = convertCoordinateToLocation(drawingModel, true, point1.x);
        float y = convertCoordinateToLocation(drawingModel, false, point1.y);
        float circleRadius;
        switch (type) {
            case WIFI:
                circleRadius = drawingModel.getPixelsPerInterval() / 4;
                paint.setColor(network.getColor());
                paint.setStyle(Style.FILL);
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
                break;
            case SENSOR:
                circleRadius = drawingModel.getPixelsPerInterval() / 5;
                paint.setColor(network.getColor());
                paint.setStyle(Style.FILL);
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
                break;
            case LTE_FEMTOCELL:
            case UMTS_FEMTOCELL:
                float dist1 = drawingModel.getPixelsPerInterval() / 3;
                float dist2 = dist1 * 4 / 6;
                paint.setStyle(Style.FILL);
                paint.setColor(network.getColor());
                Path p = new Path();
                p.reset();
                p.moveTo(x - dist1, y);
                p.lineTo(x - dist2, y - dist2);
                p.lineTo(x + dist2, y - dist2);

                p.lineTo(x + dist1, y);
                p.lineTo(x + dist2, y + dist2);
                p.lineTo(x - dist2, y + dist2);
                p.lineTo(x - dist1, y);
                p.lineTo(x - dist1, y);

                canvas.drawPath(p, paint);
                paint.setStrokeCap(Paint.Cap.ROUND);

                paint.setStyle(Style.STROKE);
                paint.setStrokeWidth(drawingModel.getPixelsPerInterval() / 16);
                paint.setColor(Color.BLACK);
                canvas.drawPath(p, paint);
                if (touch) {
                    paint.setStrokeCap(Paint.Cap.BUTT);
                    paint.setColor(Color.RED);
                    paint.setPathEffect(dottedLineEffect);
                    canvas.drawPath(p, paint);
                }
                break;
        }

        paint.reset();
    }


    @Override
    public DataObject getPartialDeepCopy() {
        return new AccessPoint(name, height, type, model, frequencyband, frequency, gain, power, network);
    }

    @Override
    public Element toXML(Document doc) {
        Element apElement = doc.createElement("accesspoint");
        apElement.setAttribute("level", "0");
        apElement.setAttribute("x", "" + point1.x);
        apElement.setAttribute("y", "" + point1.y);
        apElement.setAttribute("height", "" + height);
        apElement.setAttribute("name", name);
        Element radioElement = doc.createElement("radio");
        radioElement.setAttribute("type", type.getText());
        radioElement.setAttribute("model", model.getText());
        radioElement.setAttribute("frequency", "" + frequency.getNumber());
        radioElement.setAttribute("frequencyband", "" + frequencyband.getText());
        radioElement.setAttribute("gain", "" + gain);
        radioElement.setAttribute("power", "" + power);
        radioElement.setAttribute("network", network.getText());
        apElement.appendChild(radioElement);
        return apElement;
    }

    @Override
    public String toString() {
        if (name.isEmpty()) {
            return getPoint1() +
                    ", " + type +
                    ", " + model +
                    ", " + frequency;
        } else {
            return name;
        }
    }
}
