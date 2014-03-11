package com.ugent.networkplanningtool.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FloorPlan implements XMLTransformable{

    private List<Wall> wallList;
    private List<ConnectionPoint> connectionPointList;
    private List<AccessPoint> accessPointList;
    private List<DataActivity> dataActivityList;

    public FloorPlan() {
        wallList = new ArrayList<Wall>();
        connectionPointList = new ArrayList<ConnectionPoint>();
        accessPointList = new ArrayList<AccessPoint>();
        dataActivityList = new ArrayList<DataActivity>();
    }

    public FloorPlan(List<Wall> wallList, List<ConnectionPoint> connectionPointList, List<AccessPoint> accessPointList, List<DataActivity> dataActivityList) {
        this.wallList = wallList;
        this.connectionPointList = connectionPointList;
        this.accessPointList = accessPointList;
        this.dataActivityList = dataActivityList;
    }

    public List<Wall> getWallList() {
        return wallList;
    }

    public List<ConnectionPoint> getConnectionPointList() {
        return connectionPointList;
    }

    public List<AccessPoint> getAccessPointList() {
        return accessPointList;
    }

    public List<DataActivity> getDataActivityList() {
        return dataActivityList;
    }

    public void setWallList(List<Wall> wallList) {
        this.wallList = wallList;
    }

    public void setConnectionPointList(List<ConnectionPoint> connectionPointList) {
        this.connectionPointList = connectionPointList;
    }

    public void setAccessPointList(List<AccessPoint> accessPointList) {
        this.accessPointList = accessPointList;
    }

    public void setDataActivityList(List<DataActivity> dataActivityList) {
        this.dataActivityList = dataActivityList;
    }

    public Element toXML(Document doc) {
        Element rootElement = doc.createElement("plan");

        Element levelElement = doc.createElement("level");
        levelElement.setAttribute("number", "0");
        levelElement.setAttribute("name", "");
        rootElement.appendChild(levelElement);

        Element extraWallsElement = doc.createElement("extraWalls");
        levelElement.appendChild(extraWallsElement);
        for (Wall wall : wallList) {
            extraWallsElement.appendChild(wall.toXML(doc));
        }

        for (ConnectionPoint cp : connectionPointList) {
            levelElement.appendChild(cp.toXML(doc));
        }
        for (AccessPoint ap : accessPointList) {
            levelElement.appendChild(ap.toXML(doc));
        }
        for (DataActivity da : dataActivityList) {
            levelElement.appendChild(da.toXML(doc));
        }

        return rootElement;
    }
}
