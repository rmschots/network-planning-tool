package com.ugent.networkplanningtool.data.ServiceData;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;

import com.ugent.networkplanningtool.data.DataObject;
import com.ugent.networkplanningtool.data.XMLTransformable;
import com.ugent.networkplanningtool.model.DrawingModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CSVResult extends DataObject implements XMLTransformable {

    private final Integer level;
    private final Double download; // (mbps)
    private final Double upload; // (mbps)
    private final Double pathloss; // (db)
    private final Double powerRX; // (db)
    private final Double powerTX; // (db)
    private final Double absorption; //SAR W/kg?
    private final Double eField; // veldsterkte V/m
    private final Double pdLos;
    private final Double pdDif;
    private final Integer roomNumber;
    private final Integer drawingSize; // non-relevant; always squares of 10cm; don't draw if 0

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
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @return the download
     */
    public Double getDownload() {
        return download;
    }

    /**
     * @return the upload
     */
    public Double getUpload() {
        return upload;
    }

    /**
     * @return the pathloss
     */
    public Double getPathloss() {
        return pathloss;
    }

    /**
     * @return the powerRX
     */
    public Double getPowerRX() {
        return powerRX;
    }

    /**
     * @return the powerTX
     */
    public Double getPowerTX() {
        return powerTX;
    }

    /**
     * @return the absorption
     */
    public Double getAbsorption() {
        return absorption;
    }

    /**
     * @return the eField
     */
    public Double geteField() {
        return eField;
    }

    /**
     * @return the roomNumber
     */
    public Integer getRoomNumber() {
        return roomNumber;
    }

    /**
     * @return the drawingSize
     */
    public Integer getDrawingSize() {
        return drawingSize;
    }

    public Double getPdLos() {
        return pdLos;
    }

    public Double getPdDif() {
        return pdDif;
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

        float x = convertCoordinateToLocation(drawingModel, true, point1.x);
        float y = convertCoordinateToLocation(drawingModel, false, point1.y);

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
    public DataObject getPartialDeepCopy() {
        return new CSVResult(point1, level, download, upload, pathloss, powerRX, powerTX, absorption, eField, pdLos, pdDif, roomNumber, drawingSize);
    }

    @Override
    public Element toXML(Document doc) {
        Element resultElem = doc.createElement("result");
        resultElem.setAttribute("x", "" + point1.x);
        resultElem.setAttribute("y", "" + point1.y);
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
