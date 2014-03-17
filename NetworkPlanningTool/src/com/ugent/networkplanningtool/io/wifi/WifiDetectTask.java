package com.ugent.networkplanningtool.io.wifi;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.ugent.networkplanningtool.data.RealAccessPoint;
import com.ugent.networkplanningtool.data.enums.Frequency;
import com.ugent.networkplanningtool.io.AbstractASyncTask;
import com.ugent.networkplanningtool.layout.measure.ApLinkingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roel on 17/03/14.
 */
public class WifiDetectTask extends AbstractASyncTask<WifiManager, List<RealAccessPoint>> {

    private Context context;

    public WifiDetectTask(Context context){
        this.context = context;
    }


    @Override
    protected List<RealAccessPoint> performTaskInBackground(final WifiManager wifi) throws Exception {
        final List<RealAccessPoint> rapList = new ArrayList<RealAccessPoint>();
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("HOIZZZZZZZZ");
                final List<ScanResult> results = wifi.getScanResults();//list of access points from the last scan
                for (final ScanResult result : results) {
                    rapList.add(new RealAccessPoint(result.SSID, result.BSSID, result.capabilities, Frequency.getFreqByNumber(result.frequency), result.level));
                    System.out.println(result.SSID + ", " + result.BSSID + ", " + result.capabilities + ", " + result.describeContents() + ", " + result.frequency + ", " + result.level + ", " + result.timestamp);
                }
                synchronized (rapList){
                    rapList.notify();
                }
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifi.startScan();
        synchronized (rapList){
            rapList.wait();
        }
        return rapList;
    }
}
