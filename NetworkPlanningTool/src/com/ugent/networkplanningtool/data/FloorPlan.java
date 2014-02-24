package com.ugent.networkplanningtool.data;

import java.util.ArrayList;
import java.util.List;

public class FloorPlan {

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
}
