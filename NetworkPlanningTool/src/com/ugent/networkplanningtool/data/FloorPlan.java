package com.ugent.networkplanningtool.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all data on a floor plan
 */
public class FloorPlan implements XMLTransformable {

    private List<Wall> wallList;
    private List<ConnectionPoint> connectionPointList;
    private List<AccessPoint> accessPointList;
    private List<DataActivity> dataActivityList;

    /**
     * Default constructor
     */
    public FloorPlan() {
        wallList = new ArrayList<Wall>();
        connectionPointList = new ArrayList<ConnectionPoint>();
        accessPointList = new ArrayList<AccessPoint>();
        dataActivityList = new ArrayList<DataActivity>();
    }

    /**
     * Constructor setting all lists of data
     * @param wallList the list with Walls
     * @param connectionPointList the list with ConnectionPoints
     * @param accessPointList the list with AccessPoints
     * @param dataActivityList the list with DataActivities
     */
    public FloorPlan(List<Wall> wallList, List<ConnectionPoint> connectionPointList, List<AccessPoint> accessPointList, List<DataActivity> dataActivityList) {
        this.wallList = wallList;
        this.connectionPointList = connectionPointList;
        this.accessPointList = accessPointList;
        this.dataActivityList = dataActivityList;
    }

    /**
     * Returns the Wall list
     * @return the Wall list
     */
    public List<Wall> getWallList() {
        return wallList;
    }

    /**
     * Returns the ConnectionPoint list
     * @return the ConnectionPoint list
     */
    public List<ConnectionPoint> getConnectionPointList() {
        return connectionPointList;
    }

    /**
     * Returns the AccessPoint list
     * @return the AccessPoint list
     */
    public List<AccessPoint> getAccessPointList() {
        return accessPointList;
    }

    /**$
     * Returns the DataActivity list
     * @return the DataActivity list
     */
    public List<DataActivity> getDataActivityList() {
        return dataActivityList;
    }

    /**
     * Sets the Wall list
     * @param wallList the Wall list
     */
    public void setWallList(List<Wall> wallList) {
        this.wallList = wallList;
    }

    /**
     * Sets the ConnectionPoint list
     * @param connectionPointList the ConnectionPoint list
     */
    public void setConnectionPointList(List<ConnectionPoint> connectionPointList) {
        this.connectionPointList = connectionPointList;
    }

    /**
     * Sets the AccessPoint list
     * @param accessPointList the AccessPoint list
     */
    public void setAccessPointList(List<AccessPoint> accessPointList) {
        this.accessPointList = accessPointList;
    }

    /**
     * Sets the DataActivity list
     * @param dataActivityList the DataActivity list
     */
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
