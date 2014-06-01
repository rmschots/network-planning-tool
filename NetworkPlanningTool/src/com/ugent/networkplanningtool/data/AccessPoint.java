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

/**
 * Represents an access point
 */
public class AccessPoint extends FloorPlanObject implements XMLTransformable {

    private String name;
    private int height;

    private RadioType type;
    private RadioModel model;
    private Frequency frequency;
    private FrequencyBand frequencyband;
    private int gain;
    private int power;
    private Network network;

    private RealAccessPoint rap;

    /**
     * Constructor creating a deep copy of another AccessPoint
     * @param accessPoint the AccessPoint to create a deep copy of
     */
    public AccessPoint(AccessPoint accessPoint) {
        super(accessPoint);
        DATA_OBJECT_TYPE = FloorPlanObjectType.ACCESS_POINT;
        this.name = accessPoint.name;
        this.height = accessPoint.height;
        this.type = accessPoint.type;
        this.model = accessPoint.model;
        this.frequency = accessPoint.frequency;
        this.frequencyband = accessPoint.frequencyband;
        this.gain = accessPoint.gain;
        this.power = accessPoint.power;
        this.network = accessPoint.network;

        this.rap = new RealAccessPoint(accessPoint.getRap());
    }

    /**
     * Constructor setting all variables except for the location
     * @param name the name of the access point
     * @param height the height at which the access point is located
     * @param type the type of the access point
     * @param model the model of the access point
     * @param frequencyband the frequencyband of the access point
     * @param frequency the frequency of the access point
     * @param gain the gain of the access point
     * @param power the power of the access point
     * @param network the network of the access point
     */
    public AccessPoint(String name, int height, RadioType type,
                       RadioModel model, FrequencyBand frequencyband, Frequency frequency, int gain,
                       int power, Network network) {
        super();
        DATA_OBJECT_TYPE = FloorPlanObjectType.ACCESS_POINT;
        this.name = name;
        this.height = height;
        this.type = type;
        this.model = model;
        this.frequency = frequency;
        this.frequencyband = frequencyband;
        this.gain = gain;
        this.power = power;
        this.network = network;

        this.rap = RealAccessPoint.getEmptyDummy();
    }

    /**
     * Constructor setting all variables including the location
     * @param point the location
     * @param name the name of the access point
     * @param height the height at which the access point is located
     * @param type the type of the access point
     * @param model the model of the access point
     * @param frequencyband the frequencyband of the access point
     * @param frequency the frequency of the access point
     * @param gain the gain of the access point
     * @param power the power of the access point
     * @param network the network of the access point
     */
    public AccessPoint(Point point, String name, int height, RadioType type,
                       RadioModel model, FrequencyBand frequencyband, Frequency frequency, int gain,
                       int power, Network network) {
        super(point);
        DATA_OBJECT_TYPE = FloorPlanObjectType.ACCESS_POINT;
        this.name = name;
        this.height = height;
        this.type = type;
        this.model = model;
        this.frequency = frequency;
        this.frequencyband = frequencyband;
        this.gain = gain;
        this.power = power;
        this.network = network;

        this.rap = RealAccessPoint.getEmptyDummy();
    }

    /**
     * Returns the name of the access point
     * @return the name of the access point
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the access point
     * @param name the name of the access point
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the height at which the access point is located
     * @return the height at which the access point is located
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height at which the access point is located
     * @param height the height at which the access point is located
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Returns the type of the access point
     * @return the type of the access point
     */
    public RadioType getType() {
        return type;
    }

    /**
     * Sets the type of the access point
     * @param type the type of the access point
     */
    public void setType(RadioType type) {
        this.type = type;
    }

    /**
     * Returns the model of the access point
     * @return the model of the access point
     */
    public RadioModel getModel() {
        return model;
    }

    /**
     * Sets the model of the access point
     * @param model the model of the access point
     */
    public void setModel(RadioModel model) {
        this.model = model;
    }

    /**
     * Returns the frequency of the access point
     * @return the frequency of the access point
     */
    public Frequency getFrequency() {
        return frequency;
    }

    /**
     * Sets the frequency of the access point
     * @param frequency the frequency of the access point
     */
    public void setFrequency(Frequency frequency) {
        if (!frequency.equals(rap.getFrequency())) {
            rap = RealAccessPoint.getEmptyDummy();
        }
        this.frequency = frequency;
    }

    /**
     * Returns the frequancyband of the access point
     * @return the frequancyband of the access point
     */
    public FrequencyBand getFrequencyband() {
        return frequencyband;
    }

    /**
     * Sets the frequancyband of the access point
     * @param frequencyband the frequancyband of the access point
     */
    public void setFrequencyband(FrequencyBand frequencyband) {
        this.frequencyband = frequencyband;
    }

    /**
     * Returns the gain of the access point
     * @return the gain of the access point
     */
    public int getGain() {
        return gain;
    }

    /**
     * Returns the gain of the access point
     * @param gain the gain of the access point
     */
    public void setGain(int gain) {
        this.gain = gain;
    }

    /**
     * Returns the power of the access point
     * @return the power of the access point
     */
    public int getPower() {
        return power;
    }

    /**
     * Sets the power of the access point
     * @param power the power of the access point
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * Returns the network of the access point
     * @return the network of the access point
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * Sets the network of the access point
     * @param network the network of the access point
     */
    public void setNetwork(Network network) {
        this.network = network;
    }

    /**
     * Returns the real access point linked to this drawn access point
     * @return the real access point linked to this drawn access point
     */
    public RealAccessPoint getRap() {
        return rap;
    }

    /**
     * Sets the real access point linked to this drawn access point
     * @param rap the real access point linked to this drawn access point
     */
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
        float x = drawingModel.convertCoordinateToLocation(true, point1.x);
        float y = drawingModel.convertCoordinateToLocation(false, point1.y);
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
    public FloorPlanObject getPartialDeepCopy() {
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
