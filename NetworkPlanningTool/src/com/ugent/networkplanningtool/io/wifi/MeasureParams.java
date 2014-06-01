package com.ugent.networkplanningtool.io.wifi;

import com.ugent.networkplanningtool.data.AccessPoint;

import java.util.List;

/**
 * Parameters for the task sampling the signal strength
 */
public class MeasureParams {

    private int sampleCount;
    private List<AccessPoint> accessPointList;

    /**
     * Default constructor
     * @param sampleCount the amount of samples to take
     * @param accessPointList the list of access points to take samples of
     */
    public MeasureParams(int sampleCount, List<AccessPoint> accessPointList) {
        this.sampleCount = sampleCount;
        this.accessPointList = accessPointList;
    }

    /**
     * returns the amount of samples
     * @return the amount of samples
     */
    public int getSampleCount() {
        return sampleCount;
    }

    /**
     * Returns the list of access points to take samples of
     * @return the list of access points to take samples of
     */
    public List<AccessPoint> getAccessPointList() {
        return accessPointList;
    }
}
