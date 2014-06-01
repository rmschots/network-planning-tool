package com.ugent.networkplanningtool.data.ServiceData;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;

import com.ugent.networkplanningtool.data.ApMeasurement;
import com.ugent.networkplanningtool.data.FloorPlanObject;
import com.ugent.networkplanningtool.data.XMLTransformable;
import com.ugent.networkplanningtool.model.DrawingModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Contains location specific web service results
 */
public class CSVResult extends FloorPlanObject implements XMLTransformable {

    private ApMeasurement apMeasurement;
    private final Integer level;
    private final Double download; // (mbps)
    private final Double upload; // (mbps)
    private final Double pathloss; // (db)
    private Double powerRX; // (db)
    private final Double powerTX; // (db)
    private final Double absorption; //SAR W/kg?
    private final Double eField; // veldsterkte V/m
    private final Double pdLos;
    private final Double pdDif;
    private final Integer roomNumber;
    private final Integer drawingSize; // non-relevant; always squares of 10cm; don't draw if 0

    /**
     * Constructor setting all variables
     * @param point the location of the results
     * @param level the floor
     * @param download the download speed
     * @param upload the upload speed
     * @param pathloss the path loss
     * @param powerRX the received power
     * @param powerTX the transmit power
     * @param absorption the absorption
     * @param eField the electric field
     * @param pdLos not used
     * @param pdDif not used
     * @param roomNumber the room number
     * @param drawingSize not used
     */
    public CSVResult(Point point, Integer level, Double download, Double upload, Double pathloss, Double powerRX, Double powerTX, Double absorption, Double eField, Double pdLos, Double pdDif, Integer roomNumber, Integer drawingSize) {
        super(point);
        this.level = level;
        this.download = download;
        this.upload = upload;
        this.pathloss = pathloss;
        this.powerRX = powerRX;
        this.powerTX = powerTX;
        this.absorption = absorption;
        this.eField = eField;
        this.pdLos = pdLos;
        this.pdDif = pdDif;
        this.roomNumber = roomNumber;
        this.drawingSize = drawingSize;
    }

    /**
     * Returns the real device measurement at this location
     * @return the real device measurement at this location
     */
    public ApMeasurement getApMeasurement() {
        return apMeasurement;
    }

    public void setApMeasurement(ApMeasurement apMeasurement) {
        this.apMeasurement = apMeasurement;
    }

    /**
     * Returns the floor
     * @return the floor
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Returns the download speed
     * @return the download speed
     */
    public Double getDownload() {
        return download;
    }

    /**
     * Returns the upload speed
     * @return the upload speed
     */
    public Double getUpload() {
        return upload;
    }

    /**
     * Returns the path loss
     * @return the path loss
     */
    public Double getPathloss() {
        return pathloss;
    }

    /**
     * Returns the received power
     * @return the received power
     */
    public Double getPowerRX() {
        return powerRX;
    }

    /**
     * Returns the transmit power
     * @return the transmit power
     */
    public Double getPowerTX() {
        return powerTX;
    }

    /**
     * returns the absorption
     * @return the absorption
     */
    public Double getAbsorption() {
        return absorption;
    }

    /**
     * Returns the electric field
     * @return the electric field
     */
    public Double geteField() {
        return eField;
    }

    /**
     * Returns the room number
     * @return the room number
     */
    public Integer getRoomNumber() {
        return roomNumber;
    }

    /**
     * Sets the received power (used by shifting with real measurements)
     * @param powerRX the new received power
     */
    public void setPowerRX(Double powerRX) {
        this.powerRX = powerRX;
    }

    @Override
    public String toString() {
        return "CSVResult{" +
                "level=" + level +
                ", download=" + download +
                ", upload=" + upload +
                ", pathloss=" + pathloss +
                ", powerRX=" + powerRX +
                ", powerTX=" + powerTX +
                ", absorption=" + absorption +
                ", eField=" + eField +
                ", pdLos=" + pdLos +
                ", pdDif=" + pdDif +
                ", roomNumber=" + roomNumber +
                ", drawingSize=" + drawingSize +
                '}';
    }

    @Override
    public void drawOnCanvas(Canvas canvas, DrawingModel drawingModel, Paint paint, boolean touch) {
        float circleRadius = drawingModel.getPixelsPerInterval() / 3;

        float x = drawingModel.convertCoordinateToLocation(true, point1.x);
        float y = drawingModel.convertCoordinateToLocation(false, point1.y);

        //paint.setStyle(Style.FILL);
        RadialGradient gradient = new RadialGradient(x, y, circleRadius, Color.rgb((int) (eField * 50), (int) (eField * 50), (int) (eField * 50)),
                Color.TRANSPARENT, android.graphics.Shader.TileMode.CLAMP);
        paint.setDither(true);
        paint.setShader(gradient);
        canvas.drawCircle(x, y, circleRadius, paint);
        paint.setStrokeWidth(drawingModel.getPixelsPerInterval() / 4);

        paint.reset();
    }

    @Override
    public FloorPlanObject getPartialDeepCopy() {
        return new CSVResult(point1, level, download, upload, pathloss, powerRX, powerTX, absorption, eField, pdLos, pdDif, roomNumber, drawingSize);
    }

    @Override
    public Element toXML(Document doc) {
        Element resultElem = doc.createElement("result");
        resultElem.setAttribute("x", "" + point1.x);
        resultElem.setAttribute("y", "" + point1.y);
        if(apMeasurement != null){
            resultElem.setAttribute("realX",""+apMeasurement.getPoint1().x);
            resultElem.setAttribute("realY",""+apMeasurement.getPoint1().y);
        }
        resultElem.setAttribute("level",""+level);
        resultElem.setAttribute("download",""+download);
        resultElem.setAttribute("upload",""+upload);
        resultElem.setAttribute("pathloss",""+pathloss);
        resultElem.setAttribute("powerRX",""+powerRX);
        resultElem.setAttribute("powerTX",""+powerTX);
        resultElem.setAttribute("absorption",""+absorption);
        resultElem.setAttribute("eField",""+eField);
        resultElem.setAttribute("pdLos",""+pdLos);
        resultElem.setAttribute("pdDif",""+pdDif);
        resultElem.setAttribute("roomNumber",""+roomNumber);
        resultElem.setAttribute("drawingSize",""+drawingSize);
        return resultElem;
    }

}
