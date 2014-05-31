package com.ugent.networkplanningtool.io.wifi;

import com.ugent.networkplanningtool.data.AccessPoint;

import java.util.List;

public class MeasureParams {

    private int sampleCount;
    private List<AccessPoint> accessPointList;

    public MeasureParams(int sampleCount, List<AccessPoint> accessPointList) {
        this.sampleCount = sampleCount;
        this.accessPointList = accessPointList;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public List<AccessPoint> getAccessPointList() {
        return accessPointList;
    }
}
