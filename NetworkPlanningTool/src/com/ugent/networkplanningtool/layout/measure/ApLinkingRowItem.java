package com.ugent.networkplanningtool.layout.measure;

import android.widget.ArrayAdapter;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.RealAccessPoint;

/**
 * Object containing the data for a linking object in the linking list.
 */
public class ApLinkingRowItem {
    private AccessPoint ap;
    private ArrayAdapter<RealAccessPoint> apAdapter;

    /**
     * Constructor setting a drawn access point and its possible real access points to link with
     * @param ap a drawn access point
     * @param apAdapter containing the possible real detected access points to link with
     */
    public ApLinkingRowItem(AccessPoint ap, ArrayAdapter<RealAccessPoint> apAdapter) {
        this.ap = ap;
        this.apAdapter = apAdapter;
    }

    /**
     * Returns the drawn access point
     * @return the drawn access point
     */
    public AccessPoint getAp() {
        return ap;
    }

    /**
     * Sets the drawn access point
     * @param ap the drawn access point
     */
    public void setAp(AccessPoint ap) {
        this.ap = ap;
    }

    /**
     * returns the possible real detected access points to link with
     * @return the possible real detected access points to link with
     */
    public ArrayAdapter<RealAccessPoint> getApAdapter() {
        return apAdapter;
    }

    /**
     * Sets the possible real detected access points to link with
     * @param apAdapter the possible real detected access points to link with
     */
    public void setApAdapter(ArrayAdapter<RealAccessPoint> apAdapter) {
        this.apAdapter = apAdapter;
    }
}
