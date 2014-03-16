package com.ugent.networkplanningtool.data;

import com.ugent.networkplanningtool.data.enums.Frequency;

/**
 * Created by Roel on 15/03/14.
 */
public class RealAccessPoint {

    private String ssid;
    private String BSSID;
    private String capabilities;
    private Frequency frequency;
    private int signalStrength;

    public RealAccessPoint(String ssid, String BSSID, String capabilities, Frequency frequency, int signalStrength) {
        this.ssid = ssid;
        this.BSSID = BSSID;
        this.capabilities = capabilities;
        this.frequency = frequency;
        this.signalStrength = signalStrength;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealAccessPoint that = (RealAccessPoint) o;

        if (!BSSID.equals(that.BSSID)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return BSSID.hashCode();
    }

    @Override
    public String toString() {
        return ssid + ", " + signalStrength + " dBm";
    }
}
