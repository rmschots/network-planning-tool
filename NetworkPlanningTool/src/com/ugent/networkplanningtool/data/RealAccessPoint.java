package com.ugent.networkplanningtool.data;

import com.ugent.networkplanningtool.data.enums.Frequency;

/**
 * Represents a real access point, detected by the device
 */
public class RealAccessPoint {

    private static RealAccessPoint emptyDummy = new RealAccessPoint("", "", "", null, 0);

    private String ssid;
    private String BSSID;
    private String capabilities;
    private Frequency frequency;
    private int signalStrength;

    /**
     * Constructor creating a deep copy of another RealAccessPoint
     * @param rap the RealAccessPoint to create a deep copy of
     */
    public RealAccessPoint(RealAccessPoint rap) {
        this.ssid = rap.getSsid();
        this.BSSID = rap.getBSSID();
        this.capabilities = rap.getCapabilities();
        this.frequency = rap.getFrequency();
        this.signalStrength = rap.getSignalStrength();
    }

    /**
     * Constructor take all variables as parameter
     * @param ssid the ssid of the access point
     * @param BSSID the bssid of the access point
     * @param capabilities the capabilities of the access point
     * @param frequency the frequency of the access point
     * @param signalStrength the received signal strength measured by the device
     */
    public RealAccessPoint(String ssid, String BSSID, String capabilities, Frequency frequency, int signalStrength) {
        this.ssid = ssid;
        this.BSSID = BSSID;
        this.capabilities = capabilities;
        this.frequency = frequency;
        this.signalStrength = signalStrength;
    }

    /**
     * Returns the empty dummy instance
     * @return the empty dummy instance
     */
    public static RealAccessPoint getEmptyDummy() {
        return emptyDummy;
    }

    /**
     * Returns the ssid of the access point
     * @return the ssid of the access point
     */
    public String getSsid() {
        return ssid;
    }

    /**
     * Sets the ssid of the access point
     * @param ssid the ssid of the access point
     */
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    /**
     * Returns the bssid of the access point
     * @return the bssid of the access point
     */
    public String getBSSID() {
        return BSSID;
    }

    /**
     * Sets the bssid of the access point
     * @param BSSID the bssid of the access point
     */
    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    /**
     * Returns the capabilities of the access point
     * @return the capabilities of the access point
     */
    public String getCapabilities() {
        return capabilities;
    }

    /**
     * Sets the capabilities of the access point
     * @param capabilities the capabilities of the access point
     */
    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
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
        this.frequency = frequency;
    }

    /**
     * Returns the signal strength of the access point
     * @return the signal strength of the access point
     */
    public int getSignalStrength() {
        return signalStrength;
    }

    /**
     * Sets the signal strength of the access point
     * @param signalStrength the signal strength of the access point
     */
    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealAccessPoint that = (RealAccessPoint) o;

        return BSSID.equals(that.BSSID);

    }

    @Override
    public int hashCode() {
        return BSSID.hashCode();
    }

    @Override
    public String toString() {
        if (this == emptyDummy) {
            return "";
        }
        return ssid + ", " + signalStrength + " dBm";
    }
}
