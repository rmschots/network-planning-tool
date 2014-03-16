package com.ugent.networkplanningtool.layout.measure;

import android.widget.ArrayAdapter;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.RealAccessPoint;

/**
 * Created by Roel on 15/03/14.
 */
public class ApLinkingRowItem {
    private AccessPoint ap;
    private ArrayAdapter<RealAccessPoint> apAdapter;

    public ApLinkingRowItem(AccessPoint ap, ArrayAdapter<RealAccessPoint> apAdapter) {
        this.ap = ap;
        this.apAdapter = apAdapter;
    }

    public AccessPoint getAp() {
        return ap;
    }

    public void setAp(AccessPoint ap) {
        this.ap = ap;
    }

    public ArrayAdapter<RealAccessPoint> getApAdapter() {
        return apAdapter;
    }

    public void setApAdapter(ArrayAdapter<RealAccessPoint> apAdapter) {
        this.apAdapter = apAdapter;
    }
}
