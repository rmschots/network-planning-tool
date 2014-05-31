package com.ugent.networkplanningtool.io.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.ugent.networkplanningtool.data.RealAccessPoint;
import com.ugent.networkplanningtool.data.enums.Frequency;
import com.ugent.networkplanningtool.io.AbstractASyncTask;

import java.util.ArrayList;
import java.util.List;

public class WifiDetectTask extends AbstractASyncTask<WifiManager, List<RealAccessPoint>> {

    private Context context;
    private final List<RealAccessPoint> rapList = new ArrayList<RealAccessPoint>();

    public WifiDetectTask(Context context){
        this.context = context;
    }


    @Override
    protected List<RealAccessPoint> performTaskInBackground(final WifiManager wifi) throws Exception {

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
